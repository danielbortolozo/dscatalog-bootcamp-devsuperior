import React from 'react';
import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';
import Admin from './pages/Admin';
import Catalog from './pages/Catalog';
import Home from './pages/Home';
import Navbar from './core/components/navbar';
import ProductDetail from './pages/Catalog/components/ProductDetail'
import Auth from './pages/Auth'

const Routes = () => (
  <BrowserRouter>
    <Navbar/>
      <Switch>
        <Route path="/" exact>
            <Home/>
        </Route>
        <Route path="/products" exact>
            <Catalog/>
        </Route>
        <Route path="/products/:productId">
            <ProductDetail/>
        </Route>
        <Redirect from="/admin/auth" to="/admin/auth/login" exact/>
        <Route path="/admin/auth">
            <Auth/>
        </Route>
        <Redirect from="/admin" to="/admin/products" exact/>
        <Route path="/admin">
            <Admin/>
        </Route>
      </Switch>      
  </BrowserRouter>

);

export default Routes;