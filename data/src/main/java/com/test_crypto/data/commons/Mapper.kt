package com.test_crypto.data.commons

interface Mapper<I, O> {
    fun map(data: I): O
}

interface MapperSync<DTO, ENTITY, DB> : Mapper<DTO, ENTITY> {
     fun mapToDb(data : DTO) : DB

     fun mapDbToEntity(data : DB?) : ENTITY?
}