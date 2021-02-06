import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import List from './List';
import Form from './Form';


const Products = () => {

   return (
       <div>
           
           <Switch>
           <BrowserRouter>
           <Route path="/admin/products" exact>
              <List />              
           </Route>
           <Route path="/admin/products/create">
              <Form />              
           </Route>
           <Route path="/admin/products/:productId">
              <h1>editar</h1>
           </Route>     
            
         </BrowserRouter>   
         </Switch>

       </div>
    );
}

export default Products;