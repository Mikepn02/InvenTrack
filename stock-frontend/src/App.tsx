import { MantineProvider } from "@mantine/core";
import "@mantine/core/styles.css";
import { Notifications } from "@mantine/notifications";
import "@mantine/notifications/styles.css";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import { RecoilRoot } from "recoil";
import Login from "./pages/Login";
import Home from "./pages/Home";
import Register from "./pages/Register";
import DashboardLayout from "./components/layouts/DashboardLayout";
import Overview from "./pages/dashboard/Overview";
import { AuthProvider } from "./hooks/useAuth";
import Order from "./pages/dashboard/Order";
import Stock from "./pages/dashboard/Stock";
import Settings from "./pages/dashboard/Settings";
import Analytics from "./pages/dashboard/Analytics";
import { Profile } from "./pages/dashboard/Proifle";
import Supplier from "./pages/dashboard/Supplier";

function App() {
  return (
    <RecoilRoot>
      <MantineProvider>
        <Notifications />
        <Router>
          <AuthProvider>
            <Routes>
              <Route path="" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/dashboard" element={<DashboardLayout />}>
                <Route path="" element={<Overview />} />
                <Route path="order" element={<Order />} />
                <Route path="supplier" element={<Supplier />} />
                <Route path="stock" element={<Stock />} />
                <Route path="setting" element={<Settings />} />
                <Route path="notification" element={<Notifications />} />
                <Route path="analytics" element={<Analytics />} />
                <Route path="profile" element={<Profile />}/>
              </Route>
            </Routes>
          </AuthProvider>
        </Router>
      </MantineProvider>
    </RecoilRoot>
  );
}

export default App;
