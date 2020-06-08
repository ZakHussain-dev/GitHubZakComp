package com.zhussain.githubzakcomponent.ui.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zhussain.githubzakcomponent.repository.RepoRepository
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class RepoViewModelTest{

    // Executes each task synchronously using Architecture Components.

    @Rule
    @JvmField

    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repoRepository = mock(RepoRepository::class.java)
    private val repoViewModel = RepoViewModel(repoRepository)

    @Test
    fun testNull(){
        assertThat(repoViewModel.repo, notNullValue())
        assertThat(repoViewModel.contributors, notNullValue())
        verify(repoRepository, never()).loadRepo(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
    }
}