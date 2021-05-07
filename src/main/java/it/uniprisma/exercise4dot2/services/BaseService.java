package it.uniprisma.exercise4dot2.services;

import com.google.gson.Gson;
import it.uniprisma.exercise4dot2.components.ConfigurationComponent;
import it.uniprisma.exercise4dot2.models.PagedResponse;
import it.uniprisma.exercise4dot2.utils.ConflictException;
import it.uniprisma.exercise4dot2.utils.NotFoundException;
import it.uniprisma.exercise4dot2.utils.NotValidOffsetOrLimit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class BaseService<T> {

    protected ConfigurationComponent config;
    protected List<T> list = new ArrayList<>();
    protected Gson gson;

    @SneakyThrows
    protected Resource init(Type obj, String pathFile, List<T> list) {
        if (!Files.exists(Paths.get(config.getDataPath())))
            Files.createDirectory(Paths.get(config.getDataPath()));
        try {
            File file = new File(config.getDataPath() + pathFile);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResourceLoader rl = new DefaultResourceLoader();
        Resource resource = rl.getResource("file:" + config.getDataPath() + pathFile);
        if (resource.exists()) {
            try {
                Stream<String> lines = Files.lines(resource.getFile().toPath());
                lines.forEach(l -> list.add(gson.fromJson(l, obj)));
            } catch (IOException e) {
                log.error("Error reading structures file with cause: {}", e.getMessage());
            }
        }
        return resource;
    }


    @SneakyThrows
    protected T createNew(T obj, Resource resource) {
        if (!list.contains(obj)) {
            list.add(obj);
            Files.writeString(resource.getFile().toPath(), gson.toJson(obj).concat(System.lineSeparator()), StandardOpenOption.APPEND);
            return obj;
        }
        throw new ConflictException("id", invokeGetMethod(obj).toString());
    }

    protected T getSingle(String id) {
        return list.stream()
                .filter(Objects::nonNull)
                .filter(t -> invokeGetMethod(t).toString().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(id));
    }

    @SneakyThrows
    protected void deleteSingle(String id, Resource resource) {
        T objToReturn = list.stream()
                .filter(Objects::nonNull)
                .filter(t -> invokeGetMethod(t).toString().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(id));
        list.remove(objToReturn);
        String updatedList = list.stream()
                .map(t -> gson.toJson(t))
                .collect(Collectors.joining(System.lineSeparator()));
        Files.writeString(resource.getFile().toPath(), updatedList+System.lineSeparator(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    protected T updateSingle(T obj, String id, Resource resource) {
        deleteSingle(id, resource);
        createNew(obj, resource);
        return obj;
    }


    protected Object invokeGetMethod(T objOfMethod) {
        return Arrays.stream(objOfMethod.getClass().getMethods())
                .filter(method -> method.getName().contains("getId"))
                .map(method -> {
                    try {
                        return method.invoke(objOfMethod);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .findFirst().get();
    }



    protected PagedResponse<T> findPage(List<T> filteredList, Integer index, Integer limit) {
        List<T> pageContent;
        limit = handleLimit(limit);
        index = Optional.ofNullable(index).orElse(0);
        int start = index * limit;
        int end = start + limit;
        if (start > filteredList.size() || limit < 0 || start < 0) {
            if (start > filteredList.size() || start<0) {
                throw new NotValidOffsetOrLimit("offset", index);
            }
            throw new NotValidOffsetOrLimit("limit", index);
        } else if (end > filteredList.size()) {
            end = filteredList.size();
            pageContent = filteredList.subList(start, filteredList.size());
        } else
            pageContent = filteredList.subList(start, end);

        if(pageContent.size()<=0){
            throw new NotFoundException();
        }
        return PagedResponse.<T>builder()
                .data(pageContent)
                .index(index + " of " + (int) Math.ceil(((double) filteredList.size() / (double) limit) - 1))
                .elements("from " + start + " to " + end)
                .totalElements((long) filteredList.size())
                .build();
    }


    protected Integer handleLimit(Integer limit) {
        limit = Optional.ofNullable(limit)
                .filter(l -> l <= config.getMaxPageLimit())
                .orElse(config.getDefaultPageLimit());

        if (limit > list.size()) {
            limit = list.size();
        }
        return limit;
    }


}
