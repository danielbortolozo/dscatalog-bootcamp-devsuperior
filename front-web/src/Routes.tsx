import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Admin from './pages/Admin';
import Catalog from './pages/Catalog';
import Home from './pages/Home';
import Navbar from './core/components/navbar';

const Routes = () => (
  <BrowserRouter>
    <Navbar/>
      <Switch>
        <Route path="/" exact>
            <Home/>
        </Route>
        <Route path="/catalog">
            <Catalog/>
        </Route>
        <Route path="/admin">
            <Admin/>
        </Route>
      </Switch>      
  </BrowserRouter>

);

export default Routes;