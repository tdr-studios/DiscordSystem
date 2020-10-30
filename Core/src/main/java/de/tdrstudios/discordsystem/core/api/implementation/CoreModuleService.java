package de.tdrstudios.discordsystem.core.api.implementation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.modules.*;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.utils.MethodCriteria;
import de.tdrstudios.discordsystem.utils.ReflectionUtils;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import sun.reflect.Reflection;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collector;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
@CreateService
public class CoreModuleService implements ModuleService {

    private List<Module> modules = new ArrayList<>();
    @Inject
    private Injector injector;

    @Override
    public void loadModules(File folder) {
        if (!folder.isDirectory()) return;
        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                Module module = loadFile(file);
                if (module == null) continue;
                if (getModule(module.getName()) != null) continue;
                modules.add(module);
            }
        }
    }

    private List<Module> getLoadOrder() {
        List<Module> moduleLoadOrder = new ArrayList<>();
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int remove = 1;
            int order = i;
            if (moduleLoadOrder.contains(module)) order = moduleLoadOrder.indexOf(module);
            if (i == 0) remove = 0;
            for (String dependency : module.getDependencies()) {
                if (dependency.equals(module.getName())) continue;
                Module m = getModule(dependency);
                moduleLoadOrder.add(order-remove, m);
            }
            moduleLoadOrder.add(order, module);
        }
        return moduleLoadOrder;
    }

    private Module loadFile(File file) {
        if (file.isDirectory()) return null;
        System.out.println("Loading "+file.getName());
        JarFile jarFile;
        try {
            jarFile = new JarFile(file);
        } catch (IOException e) {
            throw new UnsupportedOperationException(file.getName()+" is no Jar file!");
        }
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory()) continue;
            if (entry.getName().endsWith(".class")) {
                ClassFile classFile = null;
                try {
                    classFile = new ClassFile(new DataInputStream(jarFile.getInputStream(entry)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!classFile.getSuperclass().equals(Module.class.getName())) continue;
                AnnotationsAttribute attribute = (AnnotationsAttribute) classFile.getAttribute(AnnotationsAttribute.visibleTag);
                Annotation annotation = null;
                for (Annotation anno : attribute.getAnnotations()) {
                    if (anno.getTypeName().equals(CreateModule.class.getName())) {
                        annotation = anno;
                        break;
                    }
                }
                if (annotation == null) return null;
                StringMemberValue nameValue = (StringMemberValue) annotation.getMemberValue("name");
                StringMemberValue versionValue = (StringMemberValue) annotation.getMemberValue("version");
                StringMemberValue descriptionValue = (StringMemberValue) annotation.getMemberValue("description");
                ArrayMemberValue authorValue = (ArrayMemberValue) annotation.getMemberValue("authors");
                ArrayMemberValue hardDependencyValue = (ArrayMemberValue) annotation.getMemberValue("hardDependencies");
                ArrayMemberValue softDependencyValue = (ArrayMemberValue) annotation.getMemberValue("softDependencies");

                URLClassLoader loader = (URLClassLoader) CoreModuleService.class.getClassLoader();
                Method addURL = null;
                try {
                    addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                addURL.setAccessible(true);
                try {
                    addURL.invoke(loader, file.toURI().toURL());
                } catch (IllegalAccessException | InvocationTargetException | MalformedURLException e) {
                    e.printStackTrace();
                }
                ModuleMeta meta = new ModuleMeta(
                        nameValue.getValue(),
                        versionValue.getValue(),
                        toArray(authorValue),
                        descriptionValue == null ? "": descriptionValue.getValue(),
                        toArray(hardDependencyValue),
                        toArray(softDependencyValue),
                        classFile.getName(),
                        file,
                        null
                );
                Class<?> main = null;
                try {
                    main = loader.loadClass(meta.getMainClass());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Constructor<?> constructor = null;
                for (Constructor<?> declaredConstructor : main.getDeclaredConstructors()) {
                    if (declaredConstructor.getParameterCount() == 0) {
                        constructor = declaredConstructor;
                        break;
                    }
                }
                constructor.setAccessible(true);
                Module module = null;
                try {
                    module = (Module) constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                assert module != null;
                module.init(injector, meta);
                callAction(module, ModuleAction.LOAD);
                return module;
            }
        }
        return null;
    }

    @Override
    public void unloadModules(File folder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unloadModule(Module module) {
        if (module.getMeta().getClassLoader() instanceof URLClassLoader) {
            disable(module);
            URLClassLoader loader = (URLClassLoader) module.getMeta().getClassLoader();
            try {
                loader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
    }

    @Override
    public void unloadAll() {
        for (Module module : modules) {
            unloadModule(module);
        }
    }

    @Override
    public void disableAll() {
        for (Module module : modules) {
            disable(module);
        }
    }

    @Override
    public void enableAll() {
        for (Module module : getLoadOrder()) {
            enable(module);
        }
    }

    @Override
    public void enable(Module module) {
        module.onEnable();
        callAction(module, ModuleAction.ENABLE);
    }

    @Override
    public void disable(Module module) {
        module.onDisable();
        callAction(module, ModuleAction.DISABLE);
    }

    @Override
    public Module getModule(String name) {
        name = name.toLowerCase();
        for (Module module : modules) {
            if (module.getName().toLowerCase().equals(name)) {
                return module;
            }
        }
        return null;
    }

    @Override
    public Module getModule(File file) {
        throw new UnsupportedOperationException();
    }

    private List<ClassLoader> classLoaders = new ArrayList<>();

    @Override
    public ClassLoader[] getClassLoaders() {
        return classLoaders.toArray(new ClassLoader[classLoaders.size()]);
    }

    private String[] toArray(ArrayMemberValue depend) {
        if (depend == null) return new String[0];
        MemberValue[] values = depend.getValue();
        String[] strings = new String[values.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = ((StringMemberValue) values[i]).getValue();
        }
        return strings;
    }

    public void startModules() {
        File folder = new File("modules");
        if (!folder.exists()) folder.mkdir();
        System.out.println("Loading Modules...");
        loadModules(folder);
        enableAll();
    }

    @Override
    public void callAction(ModuleAction action) {
        if (!(action == ModuleAction.DISABLE || action == ModuleAction.ENABLE || action == ModuleAction.LOAD)) {
            for (Module module : modules) {
                for (Method method : ReflectionUtils.filter(new Class[]{module.getClass()}, MethodCriteria.annotatedWith(Execute.class), MethodCriteria.parameterCount(0))) {
                    if (method.getAnnotation(Execute.class).action() == action) {
                        try {
                            method.invoke(module);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void callAction(Module module, ModuleAction action) {
        for (Method method : ReflectionUtils.filter(new Class[]{module.getClass()}, MethodCriteria.annotatedWith(Execute.class), MethodCriteria.parameterCount(0))) {
            if (method.getAnnotation(Execute.class).action() == action) {
                try {
                    method.invoke(module);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initialize() throws Exception {
    }
}
