package com.groovycalamari.services

import co.curated.CuratedIssueResponse
import spock.lang.Ignore
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.LocalDate

class HtmlGenerationServiceImplSpec extends Specification {

    @Ignore
    def "test curatedIssueAsHtml"() {
        given:
        HtmlGenerationServiceImpl service = new HtmlGenerationServiceImpl()

        when:
        Date publishedAt = Date.parse('yyyy-MM-dd', '2018-02-11')
        CuratedIssueResponse curatedIssue = new CuratedIssueResponse(number: 114,
                summary: 'Rule-engines, Drools, Groovy.eval',
                published_at: publishedAt)
        String html = service.curatedIssueAsHtml(curatedIssue)

        then:
        html == '''
<li class='item'>
  <a href='/issues/114#start'>
    <div class='item__body'>
      <h2 class='item__heading'>Issue 114</h2>
      <p class='item__title'>Rule-engines, Drools, Groovy.eval</p>
      <h4 class='item__footer'>
        <time class='published' datetime='2018-02-11'>
          <span>11th Feb</span> 2018
        </time>
      </h4>
    </div>
  </a>
</li>
'''
    }
}
