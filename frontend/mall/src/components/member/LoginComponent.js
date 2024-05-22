import { useState } from "react"
// p379 제거 import { useDispatch } from "react-redux" // p363 추가
// p372 제거 import { login } from "../../slices/loginSlice"  //p363 추가
// p379 제거 import { loginPostAsync } from "../../slices/loginSlice" // p372 추가
// p379 제거 import { useNavigate } from "react-router-dom" // p376 처리후 이동 
import useCustomLogin from "../../hooks/useCustomLogin" // p379 추가 (훅이용)
import KakaoLoginComponent from "./KakaoLoginComponent" // p411 추가 (카카오로그인)

const initState = {
    email:'',
    pw:''
  }

const LoginComponent = () => {

const [loginParam, setLoginParam] = useState({...initState})

// p379 제거 const navigate = useNavigate() // p377 추가 
// p379 제거 const dispatch = useDispatch()

const {doLogin, moveToPath} = useCustomLogin() // p379 추가 

const handleChange = (e) => {
    loginParam[e.target.name] = e.target.value

    setLoginParam({...loginParam})
}

// p363 추가
const handleClickLogin = (e) => {
    // p372 제거  dispatch(login(loginParam))
    // p380 제거 dispatch( loginPostAsync(loginParam)) // 비동기 호출 p372 추가
    // .unwrap().then(data => { // p375 추가
    //     console.log("after unwrap...")
    //     console.log(data)
    //     if(data.error){
    //         alert("이메일과 패스워드를 다시 확인하세요")
    //     }else {
    //         alert("로그인 성공")
    //         navigate({pathname:`/`}, {replace:true}) // p377 추가 뒤로 가기 했을 대 로그인 화면을 볼 수 없도록
    //     }
    // })
    doLogin(loginParam) // p380 추가 loginSlice의 비동기 호출 
    .then(data => {
      console.log(data)
      
      if(data.error) {
        alert("이메일과 패스워드를 다시 확인하세요")
      }else {
        alert("로그인 성공")
        moveToPath('/')
      }
    })
}

return (
    <div className = "border-2 border-sky-200 mt-10 m-2 p-4">
        <div className="flex justify-center">
            <div className="text-4xl m-4 p-4 font-extrabold text-blue-500">
                Login Component</div>
            </div> 
        <div className="flex justify-center">
            <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                <div className="w-full p-3 text-left font-bold">
                    Email
                </div>
                    <input className="w-full p-3 rounded-r border border-solid border-neutral-500 shadow-md" 
                    name="email"
                    type={'text'} 
                    value={loginParam.email}
                    onChange={handleChange}
                    >
                    </input>
            </div>
        </div>
        <div className="flex justify-center">
            <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                <div className="w-full p-3 text-left font-bold">Password</div>
                    <input className="w-full p-3 rounded-r border border-solid border-neutral-500 shadow-md" 
                    name="pw"
                    type={'password'} 
                    value={loginParam.pw}
                    onChange={handleChange}
                    >
                    </input>
            </div>
        </div>
        <div className="flex justify-center">
            <div className="relative mb-4 flex w-full justify-center">
                <div className="w-2/5 p-6 flex justify-center font-bold">
                    <button 
                        className="rounded p-4 w-36 bg-blue-500 text-xl  text-white"
                        onClick={handleClickLogin}   // p363 추가
                        >
                        LOGIN
                    </button>
                </div>
            </div>
        </div>
        <KakaoLoginComponent/> 
        {/* p412 카카오 추가  */}
    </div>
    );
    }
    
    export default LoginComponent;