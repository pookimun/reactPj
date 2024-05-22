import axios from "axios";
import { getCookie, setCookie  } from "./cookieUtil"; // p390 쿠키용으로 추가  p393 추가, setCookie 
import { API_SERVER_HOST } from "../api/todoApi"; // p393 추가 (토큰 갱신용)

const jwtAxios = axios.create()

const refreshJWT =  async (accessToken, refreshToken) => {  // p393 추가

  const host = API_SERVER_HOST

  const header = {headers: {"Authorization":`Bearer ${accessToken}`}}

  const res = await axios.get(`${host}/api/member/refresh?refreshToken=${refreshToken}`, header)

  console.log("-------쿠키 리플레시---------------")
  console.log(res.data)

  return res.data 
}

//before request
const beforeReq = (config) => {
    console.log("before request.............")
    const memberInfo = getCookie("member")
  
      // p 391 추가 
      if( !memberInfo ) {
        console.log("Member NOT FOUND")
        return Promise.reject(
          {response:
            {data:
              {error:"REQUIRE_LOGIN"}
            }
          }
        )
          // return config p391 제거
      }

    // p391 추가  
    const {accessToken} = memberInfo
    config.headers.Authorization = `Bearer ${accessToken}` // Authorization 헤더 처리 
    return config
}
//fail request
const requestFail = (err) => {
    console.log("request error............")
   
    return Promise.reject(err)
  }
 
//before return response
const beforeRes = async (res) => {
  console.log("before return response...........")

  console.log(res)

  //'ERROR_ACCESS_TOKEN'
  const data = res.data

  if(data && data.error ==='ERROR_ACCESS_TOKEN'){

    const memberCookieValue = getCookie("member")

    const result = await refreshJWT( memberCookieValue.accessToken, memberCookieValue.refreshToken )
    console.log("refreshJWT RESULT", result)

    memberCookieValue.accessToken = result.accessToken
    memberCookieValue.refreshToken = result.refreshToken

    setCookie("member", JSON.stringify(memberCookieValue), 1)

    //원래의 호출 p395 추가
    const originalRequest = res.config

    originalRequest.headers.Authorization = `Bearer ${result.accessToken}`

    return await axios(originalRequest)

  }

  return res
}  
//before return response p394 변경
// const beforeRes = async (res) => {
//     console.log("before return response...........")
  
//     console.log(res)
// }

//fail response
const responseFail = (err) => {
    console.log("response fail error.............")
    return Promise.reject(err);
  }

jwtAxios.interceptors.request.use( beforeReq, requestFail ) // 요청

jwtAxios.interceptors.response.use( beforeRes, responseFail) // 응답

export default jwtAxios