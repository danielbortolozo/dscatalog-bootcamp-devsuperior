import './styles.scss';
import AuthCard from '../Card';
import { Link } from 'react-router-dom';
import ButtonIcon from 'core/components/buttonicon';
import { useForm } from "react-hook-form";

type FormData = {
    email: string;
    senha: string;
}

const Login = () => {
    const { register, handleSubmit } = useForm<FormData>();
   
    const onSubmit = (data: FormData) => {
       console.log('Dados: ', data)

       console.log('Chamar api de authentication...')
    } 
    return (
        <AuthCard title="login"> 
             <form className="login-form" onSubmit={handleSubmit(onSubmit)}>
                 <input type="email" 
                 className="form-control input-base margin-botton-30" 
                 placeholder="Email"
                 
                 {...register("email", { required: true })}
            
                 />

                 <input type="password" 
                 className="form-control input-base" 
                 placeholder="Senha"
                 {...register("senha", { required: true })}
                 />

                <Link to="/admin/auth/recover" className="login-link-recover">
                   Esqueci a senha?
                </Link>

                <div className="login-submit">
                   <ButtonIcon text="Logar" / >
                </div> 
                <div className="text-center">
                    <span className="not-registered">Não tem cadastro? </span>

                    <Link to="/admin/auth/register" className="login-link-register">
                        Cadastrar
                    </Link>
                </div>    

             </form>    
        </AuthCard>
    );
};


export default Login;