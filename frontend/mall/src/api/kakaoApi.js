import axios from "axios"; // 417 추가 
import { API_SERVER_HOST } from "./todoApi"; // 419 추가

const rest_api_key =`b58e29a8966fc2dc506598d53874cc9a` //요약 정보 앱키 REST키값 

const redirect_uri =`http://localhost:3000/member/kakao` // 카카오 로그인 Redirect URI

const auth_code_path = `https://kauth.kakao.com/oauth/authorize`  // 공통사항 (기본값)

const access_token_url =`https://kauth.kakao.com/oauth/token` // p417 엑세스 토큰 추가 


export const getKakaoLoginLink = () => {

    const kakaoURL = `${auth_code_path}?client_id=${rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`;
  
    return kakaoURL
  
  }
  
  // 418 추가 
  export const getAccessToken = async (authCode) => {
  
    const header = {
     headers: {
       "Content-Type": "application/x-www-form-urlencoded",
     }
    }
    const params = {
      grant_type: "authorization_code",
      client_id: rest_api_key,
      redirect_uri: redirect_uri,
      code:authCode
    }
    
    const res = await axios.post(access_token_url, params , header)
  
    const accessToken = res.data.access_token
  
    return accessToken
  }
  
  // 419 추가 인가 코드를 이용해 api 서버를 호출하는 기능
  export const getMemberWithAccessToken = async(accessToken) => {
  
    const res = await axios.get(`${API_SERVER_HOST}/api/member/kakao?accessToken=${accessToken}`)
  
    return res.data
  
  }