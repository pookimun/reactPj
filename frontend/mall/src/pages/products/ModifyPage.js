import { useParams } from "react-router-dom";  // p283 추가
import ModifyComponent from "../../components/products/ModifyComponent"; // p283 추가

const ModifyPage = () => {

  const {pno} = useParams()

  return ( 
  <div className="p-4 w-full bg-white">
    <div className="text-3xl font-extrabold">
      Products Modify Page  
    </div>
 
    <ModifyComponent pno={pno}/>

  </div>
   );
}
 
export default ModifyPage;