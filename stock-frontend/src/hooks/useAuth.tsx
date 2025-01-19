import { createContext, useContext, useEffect, useState } from "react";
import { User } from "../types";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "../lib/axios.config";
import { deleteCookie, setCookie } from "../lib/utils";
import { notifications } from "@mantine/notifications";

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => void;
  loggingIn: boolean;
  register: (
    user: Omit<User, "id" | "createdDate" | "lastModifiedDate"> & {
      password: string;
    }
  ) => void;
  registering: boolean;
  logout: () => void;
  loggingOut: boolean;
  initialLoading: boolean;
  updatePassword: (
    email: string,
    currentPassword: string,
    newPassword: string
  ) => Promise<any>;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [loggingIn, setLoggingIn] = useState(false);
  const [registering, setRegistering] = useState(false);
  const [loggingOut, setLoggingOut] = useState(false);
  const [initialLoading, setInitialLoading] = useState(true);
  const [user, setUser] = useState<User | null>(null);

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (user) {
      setInitialLoading(false);
      return;
    }
    const fetchUser = async () => {
      try {
        const { data } = await axios.get("/auth/me");
        setUser(data.user);
      } catch (error) {
        setUser(null);
        if (location.pathname.includes("/dashboard")) navigate("/login");
      } finally {
        setInitialLoading(false);
      }
    };
    fetchUser();
  }, [location.pathname, user]);

  const login = async (email: string, password: string) => {
    setLoggingIn(true);
    try {
      const { data } = await axios.post("/auth/authenticate", {
        email,
        password,
      });

      setUser(data.user);
      setCookie("token", data.access_token, 7);

      notifications.show({
        title: "Success",
        message: "Logged in successfully",
        color: "green",
      });
      navigate("/dashboard");
    } catch (error) {
      notifications.show({
        title: "Error",
        message: "Invalid email or password",
        color: "red",
      });
    } finally {
      setLoggingIn(false);
    }
  };

  const register = async (
    user: Omit<User, "id" | "createdDate" | "lastModifiedDate"> & {
      password: string;
    }
  ) => {
    setRegistering(true);
    try {
      const { data } = await axios.post("/auth/register", user);
      if (data) {
        notifications.show({
          title: "Success",
          message: data.message || "Account created successfully",
          color: "green",
        });
        navigate("/login");
      } else {
        notifications.show({
          message: "Something went wrong",
          color: "red",
        });
      }
    } catch (error: any) {
      notifications.show({
        title: "Error",
        message: error.response?.data?.message ?? "An error occurred",
        color: "red",
      });
    } finally {
      setRegistering(false);
    }
  };

  const logout = async () => {
    setLoggingOut(true);
    try {
      await axios.get("/auth/logout");
      setUser(null);
      deleteCookie("token");
      notifications.show({
        title: "Success",
        message: "Logged out successfully",
        color: "green",
      });
      navigate("/login");
    } catch (error) {
      notifications.show({
        title: "Error",
        message: "An error occurred",
        color: "red",
      });
    } finally {
      setLoggingOut(false);
    }
  };

  const updatePassword = async (
    email: string,
    currentPassword: string,
    newPassword: string
  ): Promise<any> => {
    try {
      const { data } = await axios.put(`/auth/update-password?email=${encodeURIComponent(email)}&currentPassword=${encodeURIComponent(currentPassword)}&newPassword=${encodeURIComponent(newPassword)}`);
      notifications.show({
        title: "Success",
        message: "Password updated successfully",
        color: "green",
      });

      return data;
    } catch (error: any) {
      notifications.show({
        title: "Error",
        message: error.response?.data?.message ?? "Failed to update password",
        color: "red",
      });
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        loggingIn,
        register,
        registering,
        logout,
        loggingOut,
        initialLoading,
        updatePassword,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
