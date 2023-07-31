import React from "react";

import { Fragment } from "react";
import MainNavigation from "./MainNavigation";

type Props = {
  children?: React.ReactNode
}

const Layout: React.FC<Props> = (props) => {
  return (
    <Fragment>
      <MainNavigation />
      <main className="container mt-4">{props.children}</main>
    </Fragment>
  )
};

export default Layout;