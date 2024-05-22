// 525 제거 import { useDispatch, useSelector } from "react-redux"
import { Navigate, createSearchParams, useNavigate } from "react-router-dom" // p396추가 createSearchParams, 
// 525 제거 import { loginPostAsync, logout } from "../slices/loginSlice"
import { useRecoilState, useResetRecoilState } from "recoil"  // 525 추가 
import signinState from "../atoms/signinState" // 525 추가 
import { loginPost } from "../api/memberApi" // 525 추가 
import { removeCookie, setCookie } from "../util/cookieUtil" // 525 추가
import { cartState } from "../atoms/cartState" // 536 추가 (로그아웃시 리코일로 유지되는 장바구니 삭제) 

// Atom은 공유하고 싶은 데이터를 atom()을 이용해서 생성, 컴포넌트에서는 이를 구독함
// Atom으로 유지되는 데이터가 변경되면 이를 구독하는 컴포넌트들은 다시 렌더링이 이루어짐
// 리코일 훅 종류 useRecoilState, usesetRecoilState, useResetRecoilState 이 있다.
// useRecoilState(읽기 전용) : 가장 기본이 되는 훅 useState()와 유사하지만 범위가 애플리케이션 전체
// useSetRecoilState(쓰기 전용) ,  useResetRecoilState(초기화용)


const useCustomLogin = ( ) => {

    const navigate = useNavigate()

    const [loginState, setLoginState] = useRecoilState(signinState) // 525 추가

    const resetState = useResetRecoilState(signinState) // 525 추가

    const isLogin = loginState.email ? true :false // 525 추가 ----------로그인 여부 

    const resetCartState = useResetRecoilState(cartState) // 536 추가 장바구니 비우기  

    const doLogin = async (loginParam) => { // 525 추가----------로그인 함수 

    const result =  await loginPost(loginParam) // 525 추가

    console.log(result) // 525 추가

    saveAsCookie(result) // 525 추가

    return result // 525 추가

    // 525 제거 들  ~ 
    // const dispatch = useDispatch()

    // //p396 추가
    // const exceptionHandle = (ex) => {

    // console.log("Exception------------------------")

    // console.log(ex)

    // const errorMsg = ex.response.data.error

    // const errorStr = createSearchParams({error: errorMsg}).toString()

    //     if(errorMsg === 'REQUIRE_LOGIN'){
    //       alert("로그인 해야만 합니다.")
    //       navigate({pathname:'/member/login' , search: errorStr})

    //       return
    //     }

    //     if(ex.response.data.error === 'ERROR_ACCESSDENIED'){
    //       alert("해당 메뉴를 사용할 수 있는 권한이 없습니다.")
    //       navigate({pathname:'/member/login' , search: errorStr})
    //       return 
    //     }
    //   }


    // // 525 제거 const loginState = useSelector(state => state.loginSlice) //-------로그인 상태

    // const isLogin = loginState.email ? true :false //----------로그인 여부 
  
    // const doLogin = async (loginParam) => { //----------로그인 함수 

    // const action  = await dispatch(loginPostAsync(loginParam))

    // return action.payload

    }

    const saveAsCookie = (data) => { // 525 추가
    
      setCookie("member",JSON.stringify(data), 1) //1일

      setLoginState(data)
      
    }

    const doLogout = () => { //---------------로그아웃 함수 
        removeCookie('member')  // 526 추가
        resetState()            // 526 추가
        resetCartState()      // 536 추가
        // 526 제거 dispatch(logout())
    }

    const moveToPath = (path) => {  //----------------페이지 이동 
        navigate({pathname: path}, {replace:true})
    }

    const moveToLogin = () => { //----------------------로그인 페이지로 이동 
        navigate({pathname: '/member/login'}, {replace:true})
    }

    const moveToLoginReturn = () => { //----------------------로그인 페이지로 이동 컴포넌트 
        return <Navigate replace to="/member/login"/>
    }

    
    // 526 추가
    const exceptionHandle = (ex) => {

      console.log("Exception------------------------")
  
      console.log(ex)
  
      const errorMsg = ex.response.data.error
  
      const errorStr = createSearchParams({error: errorMsg}).toString()
  
      if(errorMsg === 'REQUIRE_LOGIN'){
        alert("로그인 해야만 합니다.")
        navigate({pathname:'/member/login' , search: errorStr})
  
        return
      }
  
      if(ex.response.data.error === 'ERROR_ACCESSDENIED'){
        alert("해당 메뉴를 사용할 수 있는 권한이 없습니다.")
        navigate({pathname:'/member/login' , search: errorStr})
        return 
      }
    }

  return  {loginState, isLogin, doLogin, doLogout, moveToPath, moveToLogin, moveToLoginReturn, exceptionHandle, saveAsCookie} // p397추가, exceptionHandle //526 추가 , saveAsCookie

}

export default useCustomLogin