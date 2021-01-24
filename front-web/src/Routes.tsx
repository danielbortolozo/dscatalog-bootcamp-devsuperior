import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Admin from './pages/Admin';
import Catalog from './pages/Catalog';
import Home from './pages/Home';
import Navbar from './core/components/navbar';
import ProductDetail from './pages/Catalog/components/ProductDetail'

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
        <Route path="/admin">
            <Admin/>
        </Route>
      </Switch>      
  </BrowserRouter>

);

export default Routes;