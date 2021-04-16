import { makePrivateRequest } from 'core/utils/request';
import { useForm } from 'react-hook-form';
import BaseForm from '../../Baseform';
import './styles.scss';

type FormState = {
    name: string;
    price: string;
    imageUrl: string;
    description: string;
}

const Form = () => {
   

    const { register, handleSubmit, formState: { errors } } = useForm<FormState>();
 //   const [hasError, setHasError] = useState(false);
    // const history = useHistory();
    // let location = useLocation<LocationState>();

        
    const onSubmit = (data: FormState) =>{
       console.log(data);
       makePrivateRequest({ url: '/products', method: 'POST', data })
        
    }
  
    return (
        <form onSubmit={handleSubmit(onSubmit)}> 
            <BaseForm title="cadastrar produto">
                <div className="row">
                    <div className="col-6">
                        <div className="margin-botton-30">
                            <input 
                                {...register("name", { 
                                    required: "Campo obrigatório",
                                    minLength: {
                                              value: 5,
                                               message: "O campo deve ter no mínimo 5 caracteres."
                                             },
                                    maxLength: {
                                                value: 60,
                                                 message: "O campo deve ter no máximo 60 caracteres."
                                               }         
                                })}                            
                                type="text" 
                                className={`form-control input-base ${errors.name ? 'is-invalid' : ''} `}                       
                                placeholder={"nome do produto"}
                            />
                             {errors.name && (
                                <div className="invalid-feedback d-block">
                                    {errors.name?.message}
                                </div>
                            )}
                        </div>
                       
                        <div className="margin-botton-30">
                            <input  
                                {...register("price", { required: "Campo obrigatório"})}                                                            
                                type="text" 
                                className={`form-control input-base ${errors.price ? 'is-invalid' : ''} `}                                    
                                placeholder={"price"}
                            />   
                             {errors.price && (
                                <div className="invalid-feedback d-block">
                                    {errors.price?.message}
                                </div>
                            )}
                        </div>

                        <div className="margin-botton-30">
                            <input 
                                {...register("imageUrl", { required: "Campo obrigatório"})}
                                type="text" 
                                className={`form-control input-base ${errors.imageUrl ? 'is-invalid' : ''} `}                              
                                placeholder={"Imagem"}
                            /> 
                             {errors.imageUrl && (
                                <div className="invalid-feedback d-block">
                                    {errors.imageUrl?.message}
                                </div>
                            )}
                        </div>

                    </div>                     


                    <div className="col-6">
                        <div>
                            <textarea 
                               {...register("description", { required: "Campo obrigatório" })} 
                               className="form-control input-base"
                               cols={30}
                               rows={10}
                            />  
                             {errors.description && (
                                <div className="invalid-feedback d-block">
                                    {errors.description?.message}
                                </div>
                            )}   
                        </div>
                    </div>       
                </div>    
            </BaseForm>
        </form>
    );
}

export default Form;