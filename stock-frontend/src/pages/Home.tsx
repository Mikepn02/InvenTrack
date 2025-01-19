import { Button } from "@mantine/core";
import React from "react";
import { Link } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const Home = () => {
  const { user } = useAuth();
  return (
    <div>
      <div className="text-center">
        <p className="text-base font-semibold text-blue-600">Hello there!</p>
        <h1 className="mt-4 text-3xl font-bold tracking-tight text-gray-900 sm:text-5xl">
          Welcome to our system
        </h1>
        <p className="mt-6 text-base leading-7 text-gray-600">
          {user
            ? `You are logged in as ${user.firstName} ${user.lastName}`
            : " Please login or create an account to continue"}
        </p>
        {user ? (
          <Link to={"/dashboard"}>
            <Button className="mt-10">Go to Dashboard</Button>
          </Link>
        ) : (
          <div className="mt-10 flex items-center justify-center gap-x-6">
            <Link to={"/login"}>
              <Button className="min-w-36">Login</Button>
            </Link>
            <Link to={"/register"}>
              <Button>Create Account</Button>
            </Link>
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
