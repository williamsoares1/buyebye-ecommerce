package com.estudos.discount.mapper.mapperStruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.databind.ObjectMapper;

@Mapper
public class MapperSt {
    MapperSt INSTANCE = Mappers.getMapper(MapperSt.class);
    ObjectMapper objectMapper = new ObjectMapper();
}
