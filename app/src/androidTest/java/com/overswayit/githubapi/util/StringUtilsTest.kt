package com.overswayit.githubapi.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for [stringifyParentsList].
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class StringUtilsTest {

    @Test
    fun stringifyParentsList_emptyList_returnEmptyString() {
        // Given empty list with no parents sha
        val emptyList: List<Pair<String, String>> = emptyList()

        // Then stringify empty list
        val stringifiedList = StringUtils.stringifyParentsList(emptyList)

        // Then stringified list is not null
        // and stringified list is empty string
        assertThat(stringifiedList, not(nullValue()))
        assertThat(stringifiedList, `is`(""))
    }

    @Test
    fun stringifyParentsList_oneParent_returnsOneParentWithOutDash() {
        // Given list with one sha parent
        val parents = ArrayList<Pair<String, String>>()
        parents.add(Pair("23db3f4", "23db3f4"))

        // Then stringify parents list
        val stringifiedList = StringUtils.stringifyParentsList(parents)

        // Then stringified list is not null or empty
        // and stringified list is "[23db3f4]"
        assertThat(stringifiedList, not(isEmptyOrNullString()))
        assertThat(stringifiedList, `is`("[23db3f4]"))
    }

    @Test
    fun stringifyParentsList_twoParents_returnTwoParentsWithDashInBetween() {
        // Given list with two sha parent
        val parents = ArrayList<Pair<String, String>>()
        parents.add(Pair("23db3f4", "23db3f4"))
        parents.add(Pair("90sad6s1", "90sad6s1"))

        // Then stringify parents list
        val stringifiedList = StringUtils.stringifyParentsList(parents)

        // Then stringified list is not null or empty
        // and stringified list is "[23db3f4] - [90sad6s1]"
        assertThat(stringifiedList, not(isEmptyOrNullString()))
        assertThat(stringifiedList, `is`("[23db3f4] - [90sad6s1]"))
    }

}