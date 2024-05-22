// 526 제거 import { useSelector } from "react-redux"; // p354 추가
import { Link } from "react-router-dom";
import useCustomLogin from "../../hooks/useCustomLogin"; // 526 추가

const BasicMenu = () => {

    const {loginState} = useCustomLogin() // 526 추가

    // 526 제거 const loginState = useSelector(state => state.loginSlice) // p355 추가

    return (
        <nav id="navbar" className=" flex bg-blue-300">
            <div className="w-4/5 bg-gray-500" >
                <ul className="flex p-4 text-white font-bold">
                    <li className="pr-6 text-2xl">
                        <Link to={'/'}>Main</Link>
                    </li>
                    <li className="pr-6 text-2xl">
                        <Link to={'/about'}>About</Link>
                    </li>

                    {/*  p355 추가설정 : 로그인 한 사용자만 출력  */}

                    {loginState.email ?  //로그인한 사용자만 출력되는 메뉴 
        
                    <>
                    <li className="pr-6 text-2xl">
                    <Link to={'/todo/'}>Todo</Link>
                    </li>
                    <li className="pr-6 text-2xl">
                    <Link to={'/products/'}>Products</Link>
                    </li>
                    </>
                    
                    :
                    <></>  
                    // 로그인 안하면 Todo, Products가 안보임
                    }

                </ul>
            </div>
        
            <div className="w-1/5 flex justify-end bg-orange-300 p-4 font-medium">
                {/* email값이 없는 경우 로그인, 있는 경우 로그아웃 처리용 */}
                { ! loginState.email ?
                    <div className="text-white text-sm m-1 rounded" >
                        <Link to={'/member/login'}>Login</Link>
                    </div>
                    : 
                    <div className="text-white text-sm m-1 rounded" >
                        <Link to={'/member/logout'}>Logout</Link> 
                        {/*  p369 추가  */}
                    </div>
                }
            </div>
                {/* <div className="text-white text-sm m-1 rounded" >
                Login
                </div> p 359 주석*/}
            
        </nav>

    );

}

export default BasicMenu;