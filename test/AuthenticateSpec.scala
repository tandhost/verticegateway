/* 
** Copyright [2012-2013] [Megam Systems]
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
** http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/
/**
 * @author rajthilak
 *
 */

package test

import org.specs2.mutable._
import org.specs2.Specification
import java.net.URL
import org.specs2.matcher.MatchResult
import org.specs2.execute.{ Result => SpecsResult }

import com.stackmob.newman.response.{ HttpResponse, HttpResponseCode }
import com.stackmob.newman._
import com.stackmob.newman.dsl._

/**
 * @author rajthilak
 *
 */

class AuthenticateSpec extends Specification {

  def is =
    "AuthenticateSpec".title ^ end ^ """
  AuthenticateSpec is the implementation that calls the megam_play API server with the /auth url
  """ ^ end ^
      "The Client Should" ^
      "Correctly do POST requests with a valid userid and api key" ! Post.succeeds ^
      // "Correctly do POST requests with a invalid userid and api key" ! PostWithInvalidUserIDEmail.succeeds ^
      // "Correctly do POST requests with an malformed header" ! PostMalformedHeader.succeeds ^
      end
  /**
   * Change the body content in method bodyToStick
   */
  case object Post extends Context {
    protected override def urlSuffix: String = "auth"
    protected override def bodyToStick: Option[String] = Some(new String("\"email\":\"chris@example.com\", \"api_key\":\"IamAtlas{74}NobodyCanSeeME#07\", \"authority\":\"user\" }"))
    protected override def headersOpt: Option[Map[String, String]]= None
    
    private val post = POST(url)(httpClient)
      .addHeaders(headers)
      .addBody(body)

    def succeeds: SpecsResult = {
      val resp = execute(post)
      resp.code must beTheSameResponseCodeAs(HttpResponseCode.Ok)
    }
  }

  case object PostWithInvalidUserIDEmail extends Context {
    protected override def urlSuffix: String = "auth"
    protected override def bodyToStick: Option[String] = Some(new String("Put the Invalid JSON as needed for auth"))
    protected override def headersOpt: Option[Map[String, String]]= None

    private val post = POST(url)(httpClient)
      .addHeaders(headers)
      .addBody(body)

    def succeeds: SpecsResult = {
      val resp = execute(post)
      resp.code must beTheSameResponseCodeAs(HttpResponseCode.Ok)
    }
  }

  case object PostMalformedHeader extends Context {
    protected override def urlSuffix: String = "auth"
    protected override def bodyToStick: Option[String] = Some(new String("Put the Invalid JSON with malformed header as needed for auth"))
    protected override def headersOpt: Option[Map[String, String]]= None
    private val post = POST(url)(httpClient)
      .addHeaders(headers)
      .addBody(body)

    def succeeds: SpecsResult = {
      val resp = execute(post)
      resp.code must beTheSameResponseCodeAs(HttpResponseCode.Ok)
    }
  }
}