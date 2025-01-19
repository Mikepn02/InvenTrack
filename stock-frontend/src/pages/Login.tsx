import React from "react";
import { Link } from "react-router-dom";
import AuthLayout from "../components/layouts/AuthLayout";
import InputField from "../components/InputField";
import { notifications } from "@mantine/notifications";
import { Button } from "@mantine/core";
import useAuth from "../hooks/useAuth";

const Login = () => {
    const { loggingIn, login } = useAuth();


    const handleSubmit = async(e : React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const form = e.currentTarget as HTMLFormElement;
        const email = form.email.value;
        const password = form.password.value

        if (!email || !password) {
            notifications.show({
                title: "Error",
                message: "Please fill in all fields",
                color: "red",
            });
            return;
        }

        login(email , password);
    }
  return (
    <>
      <AuthLayout>
        <div className="mx-auto w-full max-w-sm lg:w-96">
          <div>
            <h2 className="mt-8 text-2xl font-bold leading-9 tracking-tight text-gray-900">
              Sign in to your account
            </h2>
            <p className="mt-2 text-sm leading-6 text-gray-500">
              Not a member?{" "}
              <Link
                to="/register"
                className="font-semibold text-blue-600 hover:text-blue-500"
              >
                Create an account
              </Link>
            </p>
          </div>

          <div className="mt-10">
            <div>
              <form onSubmit={handleSubmit} className="space-y-6">
                <InputField
                  label="Email Address"
                  required
                  autoComplete="email"
                  type="email"
                  name="email"
                />

                <InputField
                  label="Password"
                  required
                  autoComplete="current-password"
                  type="password"
                  name="password"
                  min={4}
                  max={50}
                />

                <div>
                    <Button
                     type="submit"
                     className="w-full"
                     color="#1f2937"
                     loading={loggingIn}
                    >
                        Sign  in
                    </Button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </AuthLayout>
    </>
  );
};

export default Login;
