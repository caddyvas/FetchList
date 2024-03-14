package com.learn.fetchlist.utility

import com.learn.fetchlist.data.HiringList

object ExtractionUtility {

    init {
        println("Class invoked")
    }

    /** Function which does the following
    create an attribute list - Id from object list
    sort the list in ascending
    map one list to another
    remove duplicates
     */
    fun getListOfIdsFromHiringList(hiringList: List<HiringList>): Set<Int?> {
        val listOfIds: List<Int?> = hiringList.sortedBy { it.listId }.map { it.listId }
        return listOfIds.toSet()
    }
}