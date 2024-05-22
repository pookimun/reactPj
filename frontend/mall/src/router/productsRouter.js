import { Suspense, lazy } from "react";
import { Navigate } from "react-router-dom";  // 245 추가

const productsRouter = () => {

    // 245 추가
    const Loading = <div>Loading....</div>
    const ProductsList =  lazy(() => import("../pages/products/ListPage"))
    const ProductsAdd = lazy(() => import("../pages/products/AddPage")) //p247 추가
    const ProductRead = lazy(() => import("../pages/products/ReadPage")) //p271 추가 
    const ProductModify = lazy(() => import("../pages/products/ModifyPage")) //p280 추가
    
//  배열을 반환하는 기능
return [

    {
        path: "list",
        element: <Suspense fallback={Loading}><ProductsList/></Suspense>
      },
      {
        path: "",
        element: <Navigate replace to="/products/list"/>
      },
    // p247 추가
      {
        path: "add",
        element: <Suspense fallback={Loading}><ProductsAdd/></Suspense>
      },
      //p272 추가
      {
        path: "read/:pno",
        element: <Suspense fallback={Loading}><ProductRead/></Suspense>
      },
      // p280 추가
      {
        path: "modify/:pno",
        element: <Suspense fallback={Loading}><ProductModify/></Suspense>
      }
  

] 


}

export default productsRouter;