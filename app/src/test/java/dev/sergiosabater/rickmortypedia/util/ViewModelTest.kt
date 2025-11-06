package dev.sergiosabater.rickmortypedia.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
abstract class ViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()
}