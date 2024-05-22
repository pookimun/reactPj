import useCustomLogin from "../hooks/useCustomLogin"; // p380 추가
import BasicLayout from "../layouts/BasicLayout";


const AboutPage = () => {
    const {isLogin, moveToLoginReturn} = useCustomLogin() // p381 추가

    if(!isLogin){
        return moveToLoginReturn()
    } // p381 추가
    return(
    <BasicLayout>
    <div className="text-3xl">About Page</div>
    </BasicLayout>
    );

}
export default AboutPage;