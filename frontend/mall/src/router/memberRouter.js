import { Suspense, lazy } from "react";


const Loading = <div>Loading....</div>
const Login =  lazy(() => import("../pages/member/LoginPage"))
const LogoutPage = lazy(() => import("../pages/member/LogoutPage"))
const KakaoRedirect = lazy(() => import("../pages/member/KakaoRedirectPage")) // p411 추가
const MemberModify = lazy(() => import("../pages/member/ModifyPage")) // 434 추가

const memberRouter = () => {

    return [
      {
        path:"login",
        element: <Suspense fallback={Loading}><Login/></Suspense>
      },
      {
        path:"logout",
        element: <Suspense fallback={Loading}><LogoutPage/></Suspense>,
      }
      //   p367 추가 (로그아웃 페이지)
      ,
      {
        path:"kakao",
        element: <Suspense fallback={Loading}><KakaoRedirect/></Suspense>,
      }
      // p412 추가 
      ,
    {
      path:"modify",
      element: <Suspense fallback={Loading}><MemberModify/></Suspense>,
    },
    // 434 추가
    ]

}

export default memberRouter