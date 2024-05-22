import { useParams } from "react-router-dom"; //p275 추가
import ReadComponent from "../../components/products/ReadComponent";  //p275 추가

const ReadPage = () => {

    const {pno} = useParams()

    return (  
        <div className="p-4 w-full bg-white">
            <div className="text-3xl font-extrabold">
                Products Read Page  
            </div>
            <ReadComponent pno={pno}></ReadComponent>
        </div>
  );
}
 
export default ReadPage;