import { Route, Routes } from 'react-router-dom';
import Home from '../portal/home/home';
import { BookFlightWizard } from '../portal/book-flight/wizard/bookFlightWizard';
import DefinitionsList from '../portal/generic-instances/definitionList';
import { SubmittableForm } from '../form/submittableForm';
import MyFlights from '../portal/book-flight/myFlights';
import { WorkForm } from '../form/workForm';
import AppDefinitionList from '../portal/apps/appDefinitionList';
import { AppView } from '../portal/apps/appView';
import LoginForm from '../portal/login/loginForm';
import { PrivateRoute } from './privateRoute';
import React from 'react';

/**
 * Defines the React Router routes of the Flyable Portal.
 */
const routeConfig = [
  { path: '/', element: <Home />, private: true },
  { path: 'book-flight', element: <BookFlightWizard />, private: true },
  { path: 'process', element: <DefinitionsList scopeType="process" />, private: true },
  { path: 'process/:definitionId', element: <SubmittableForm formType="start" />, private: true },
  { path: 'case', element: <DefinitionsList scopeType="case" />, private: true },
  { path: 'case/:definitionId', element: <SubmittableForm formType="start" />, private: true },
  { path: 'my-flights', element: <MyFlights />, private: true },
  { path: 'my-flights/:instanceId', element: <WorkForm />, private: true },
  { path: 'apps', element: <AppDefinitionList />, private: true },
  { path: 'apps/:appKey', element: <AppView />, private: true },
  { path: '/login', element: <LoginForm />, private: false },
];

const routes = () => (
  <Routes>
    {routeConfig.map(({ path, element, private: isPrivate }) => (
      <Route key={path} path={path} element={isPrivate ? <PrivateRoute>{element}</PrivateRoute> : element} />
    ))}
  </Routes>
);

export default routes;
