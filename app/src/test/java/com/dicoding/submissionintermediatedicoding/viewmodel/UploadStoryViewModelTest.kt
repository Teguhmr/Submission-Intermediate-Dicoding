package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionintermediatedicoding.data.repository.UploadRepository
import com.dicoding.submissionintermediatedicoding.data.story.AddStoryResponse
import com.dicoding.submissionintermediatedicoding.utils.Resource
import com.dicoding.submissionintermediatedicoding.utils.Status
import com.dicoding.submissionintermediatedicoding.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UploadStoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var uploadRepository: UploadRepository
    private lateinit var uploadVM: UploadStoryViewModel

    @Mock
    private lateinit var dummyMockFile: MultipartBody.Part


    @Before
    fun setUp() {
        uploadVM = UploadStoryViewModel(uploadRepository)
    }

    @Test
    fun `when get message should return LiveData String`() {
        val dummyMessage = "dummyMessage"
        val expectedData = MutableLiveData<Resource<AddStoryResponse>>()
        expectedData.value = Resource.error(dummyMessage, null)

        Mockito.`when`(uploadRepository.storyUpload).thenReturn(expectedData)

        val actualData = uploadVM.storyUpload.getOrAwaitValue()

        Mockito.verify(uploadRepository).storyUpload
        assertTrue(actualData.status == Status.ERROR)
        assertEquals(actualData, expectedData.value)
    }

    @Test
    fun `verify uploadStory function is works by checking the message`() {
        val dummyToken = "dummyToken"
        val dummyData = AddStoryResponse (false, "Upload Success")
        val descriptionText = "this is description"
        val description = descriptionText.toRequestBody("text/plain".toMediaType())

        val expectedData = MutableLiveData<Resource<AddStoryResponse>>()
        expectedData.value = Resource.success(dummyData)

        uploadVM.uploadStory(
            dummyToken,
            dummyMockFile,
            description,
            null,
            null
        )

        Mockito.verify(uploadRepository).uploadStory(
            dummyToken,
            dummyMockFile,
            description,
            null,
            null
        )

        Mockito.`when`(uploadRepository.storyUpload).thenReturn(expectedData)

        val actualData = uploadVM.storyUpload.getOrAwaitValue()

        Mockito.verify(uploadRepository).storyUpload
        assertTrue(actualData.status == Status.SUCCESS)
        assertEquals(actualData, expectedData.value)
    }

    @Test
    fun `verify uploadStoryWithLocation function is works by checking the message value`() {
        val dummyToken = "dummyToken"
        val descriptionText = "this is description"
        val description = descriptionText.toRequestBody("text/plain".toMediaType())
        val dummyData = AddStoryResponse (false, "Upload Success")
        val expectedData = MutableLiveData<Resource<AddStoryResponse>>()
        expectedData.value = Resource.success(dummyData)

        uploadVM.uploadStory(
            dummyToken,
            dummyMockFile,
            description,
            42.069,
            69.420
        )

        Mockito.verify(uploadRepository).uploadStory(
            dummyToken,
            dummyMockFile,
            description,
            42.069,
            69.420
        )

        Mockito.`when`(uploadRepository.storyUpload).thenReturn(expectedData)

        val actualData = uploadVM.storyUpload.getOrAwaitValue()

        Mockito.verify(uploadRepository).storyUpload
        assertTrue(actualData.status == Status.SUCCESS)
        assertEquals(actualData, expectedData.value)
    }
}