import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"; // p371 추가 createAsyncThunk
import { loginPost } from "../api/memberApi";  // p371 추가
import { getCookie, removeCookie, setCookie } from "../util/cookieUtil"; // p384 쿠키 추가 , p387추가 removeCookie
// 스토어가 애플리케이션 내의 공유되는 상태데이터(app state)를 의미한다면 이에 대한 처리를 담당하는 것이 리듀서(reducers)라는 함수임
// reducers는 금고를 지키는 사람과 비슷함, 금고에 넣고 빼는 역할 처럼 애플리케이션 상태를 가공하는 역할을 담당함.
// 컴포넌트들은 액션이라는 것을 이용해서 리듀서를 호출하게 되는데 리듀서에서는 액션 페이로드값을 처리해서 앞으로 보관해야 할 애플리케이션 상태 데이터를 반환 함
// 리덕스 툴킷에서는 슬라이스(slice)라는 이름으로 한번에 작성할 수 있음.

const initState = {
    email:''
} 
// 애플리케이션이 유지해야 하는 상태와 이를 처리하기 위한 리듀서 함수들을 정의
// email 속성을 가진 객체를 사용해서 email 값이 있는 경우에는 로그인한 상태로 간주
// 그렇지 않으면 로그인이 되지 않는 상태로 간주함.

const loadMemberCookie = () => {  //쿠키에서 로그인 정보 로딩 

    const memberInfo =  getCookie("member")
  
    //닉네임 처리 
    if(memberInfo && memberInfo.nickname) {
      memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
    }
  
    return memberInfo
}

export const loginPostAsync = createAsyncThunk('loginPostAsync', (param) => {
    return loginPost(param)
}) // p371 추가  createAsyncThunk를 이용해 memberApi.js 에서 선언된 loginPost()를 호출하도록 구성

const loginSlice = createSlice({
    name: 'LoginSlice',
    initialState: loadMemberCookie()|| initState, //쿠키가 없다면 초깃값사용  p386추가 loadMemberCookie()|| 
    reducers: {
      login: (state, action) => {
        console.log("login.....")

        //{ 427 추가 소셜로그인 회원이 사용}
        const payload = action.payload
        setCookie("member",JSON.stringify(payload), 1) //1일
        return payload
        
        //{email, pw로 구성 p364 추가}
        // 427 제거 const data = action.payload
         
        //새로운 상태  p364추가
        // 427 제거 return {email: data.email}
  
      },
      logout: (state, action) => {
          console.log("logout....")
  
          removeCookie("member") // p387 추가(로그아웃시 쿠키제거)
        return {...initState} // p368 추가
      }
    },
    extraReducers: (builder) => {
        // p371 추가 비동기 통신의 상태(fulfilled: 완료), (pending:처리중), (rejected:에러)에 따라 동작하는 함수 작성
        builder.addCase( loginPostAsync.fulfilled, (state, action) => { 
            console.log("fulfilled")
            
            const payload = action.payload // p373 추가

            // p385 추가 정상적인 로그인시에만 저장
            if(!payload.error){
                setCookie("member",JSON.stringify(payload), 1) //1일
            }

            return payload // p373 추가
        })
        .addCase(loginPostAsync.pending, (state,action) => {
            console.log("pending")
        })
        .addCase(loginPostAsync.rejected, (state,action) => {
            console.log("rejected")
        })
    }
})

export const {login,logout} = loginSlice.actions
// loginSlice 내부에 선언된 함수들을 외부에 노출하기 위해서 export 처리함.
export default loginSlice.reducer    