import LoginComponent from "../../components/member/LoginComponent"; // p361 추가
import BasicMenu from "../../components/menus/BasicMenu";

const LoginPage = () => {
    return ( 
      <div className='fixed top-0 left-0 z-[1055] flex flex-col h-full w-full'>
  
        <BasicMenu/>
  
        <div className="w-full flex flex-wrap  h-full justify-center  items-center border-2">
            <div className="text-2xl">
            로그인페이지
            </div>
            {/* 로그인 폼 추가 */}
            <LoginComponent/>
        </div>  
      </div>
     );
  }
   
  export default LoginPage;