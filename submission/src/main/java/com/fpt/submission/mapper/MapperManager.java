package com.fpt.submission.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

public class MapperManager {

    private static ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private MapperManager() {
    }

    public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }
    public static <D, T> List<D> mapAll(final List<T> entityList, Class<D> outCLass) {
        if(entityList != null && entityList.size() >0){
            return entityList.stream()
                    .map(entity -> map(entity, outCLass))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
