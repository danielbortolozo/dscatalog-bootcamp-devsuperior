import React from 'react';
import { Switch } from 'react-router-dom';
import Navbar from './components/Navbar';
import Products from './components/Products';
import './styles.scss';
import PrivateRouter from 'core/components/Routes/PrivateRouter';

const Admin = () => (
    <div className="admin-container">  
       <Navbar />
       
       <div className="admin-content">
         <Switch>
           <PrivateRouter path="/admin/products">
              <Products />
           </PrivateRouter>
           <PrivateRouter path="/admin/categories" >
              <h1>Categories</h1>
           </PrivateRouter>
           <PrivateRouter path="/admin/users" allowedRoutes={['ROLE_ADMIN']}>
              <h1>Users</h1>
           </PrivateRouter>     
            
         </Switch>
       </div>
   </div>     
);

export default Admin;