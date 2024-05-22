import { atom } from "recoil";
import { getCookie } from "../util/cookieUtil"; // 529 추가

const initState = {
  email:'',
  nickname:'', // 529 추가 ~
  social: false,
  accessToken:'',
  refreshToken:''
}

// 529 추가~
const loadMemberCookie = () => { //쿠키에서 체크 

  const memberInfo =  getCookie("member")

  //닉네임 처리 
  if(memberInfo && memberInfo.nickname) {
    memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
  }

  return memberInfo
}

const signinState = atom({  // 초기값 설정
  key:'signinState',
  default: loadMemberCookie() || initState // 529 추가 loadMemberCookie() || 
})


export default signinState
