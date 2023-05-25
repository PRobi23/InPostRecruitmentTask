package pl.inpost.recruitmenttask.domain.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}