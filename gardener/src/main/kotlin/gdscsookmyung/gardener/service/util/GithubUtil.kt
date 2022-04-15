package gdscsookmyung.gardener.service.util

import gdscsookmyung.gardener.property.GitHubProperty
import org.kohsuke.github.*
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class GithubUtil (
    var github: GitHub?,
    val gitHubProperty: GitHubProperty,
){
    fun getCommits(userId: String): PagedIterator<GHCommit> {
        if (github == null) {
            try {
                connectToGithub(gitHubProperty.token)
            } catch (e: IOException) {
                throw IllegalArgumentException("failed to connect gitHub")
            }
        }

        val builder = github!!.searchCommits().author(userId).sort(GHCommitSearchBuilder.Sort.AUTHOR_DATE)
        val commits = builder.list().withPageSize(7)
        return commits._iterator(1)
    }

    private fun connectToGithub(token: String) {
        github = GitHubBuilder().withOAuthToken(token).build()
        github!!.checkApiUrlValidity()
    }
}