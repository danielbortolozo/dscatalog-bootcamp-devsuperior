import React from 'react';

import Admin from './pages/Admin';
import Catalog from './pages/Catalog';
import Home from './pages/Home';
import Navbar from './core/components/navbar';
import ProductDetail from './pages/Catalog/components/ProductDetail'
import Auth from './pages/Auth'
import history from './core/utils/history';
import { Redirect, Route, Router, Switch } from 'react-router-dom';

const Routes = () => (
  <Router history={ history }>
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
  </Router>

);

export default Routes;