import {RouterProvider} from "react-router-dom" ;
import root from "./router/root" ;
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";  // 496 리엑트 쿼리 클라이언트 지정
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";  // 496 리엑트 쿼리 클라이언트 지정

const queryClient = new QueryClient() // 496 리엑트 쿼리 클라이언트 지정

function App() {
  return (
    <QueryClientProvider client={queryClient}> 
    {/* 496 리엑트 쿼리 클라이언트 지정 */}

      <RouterProvider router={root}/>

      <ReactQueryDevtools initialIsOpen={true} />
      {
      /* 496 리엑트 쿼리 클라이언트 지정 : app 구동시에 개발도구를 오픈한 상태로 시작 
      크롬 아래쪽에 화면 생성됨
      */}
    
    </QueryClientProvider>
      
    );
}

export default App;
