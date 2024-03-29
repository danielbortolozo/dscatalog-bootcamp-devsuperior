import React from 'react'
import './styles.scss';
import { ReactComponent as AuthImage } from 'core/assets/images/auth.svg'
import { Route, Switch } from 'react-router-dom';
import Login from './components/Login';

const Auth = () => (
    <div className="auth-container">
       <div className="auth-info">
           <h1 className="auth-info-title"> 
               Divulgue seus produtos <br /> no DS Catalog
           </h1>
           <p className="auth-info-subtitle">
               Faça parte de nosso catálogo de divulgação e <br /> aumente a venda dos seus produtos               
           </p>
           <AuthImage  />
       </div>

       <div className="auth-content">
       <Switch>
           <Route path="/auth/login">
               <Login />             
           </Route>
           <Route path="/auth/register" exact >
              <h1>Register</h1>
           </Route>
           <Route path="/auth/recover" exact>
              <h1>Recuperacao</h1>
           </Route>     
            
         </Switch>       

        </div>
    </div>
)
export default Auth;