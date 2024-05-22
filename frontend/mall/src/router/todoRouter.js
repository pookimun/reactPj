import { Suspense, lazy } from "react";
// router 폴더에 todoRouter.js 파일을 추가해고 /todo/ 하위 설정들을 반환한다.

//p65 리다이렉션 처리 
import { Navigate } from "react-router-dom"; //고정된 링크 활용


    const Loading = <div>Loading.....</div>
    const TodoList = lazy(() => import("../pages/todo/ListPage"))
    const TodoRead = lazy(() => import("../pages/todo/ReadPage")) // p67 추가
    const TodoAdd = lazy(() => import("../pages/todo/AddPage"))   // p72 추가
    const TodoModify = lazy(() => import("../pages/todo/ModifyPage")) // p78 추가

    const todoRouter = () => {

    return [
        {
            path : "list",
            element : <Suspense fallback={Loading}><TodoList/></Suspense>
        },
        {
            path : "",
            element : <Navigate replace to="list"/>
        } // /todo/??? 이하의 경로가 지정되지 않았을 대 동작하는 빈경로의 설정 추가
        ,
        {
            path : "read/:tno",
            element : <Suspense fallback={Loading}><TodoRead/></Suspense>
        },
        {
            path : "add",
            element : <Suspense fallback={Loading}><TodoAdd/></Suspense>
        },
        {
            path: "modify/:tno",
            element: <Suspense fallback={Loading}><TodoModify/></Suspense>
        }
    ]
}
// p63 파일 분리 중
export default todoRouter;