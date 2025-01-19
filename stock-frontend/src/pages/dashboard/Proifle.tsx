import React, { useState } from "react";
import InputField from "../../components/InputField";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../../components/ui/card";
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from "../../components/ui/tabs";
import useAuth from "../../hooks/useAuth";
import Steps from "../../components/Steps";
import { Button } from "@mantine/core";
import { notifications } from "@mantine/notifications";

export function Profile() {
  const { user, updatePassword, logout } = useAuth();
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleUpdatePassword = () => {
    if (newPassword !== confirmPassword) {
      notifications.show({
        title: "Error",
        message: "New password and confirm password do not match",
        color: "red",
      });
      return;
    }
    updatePassword(user?.email || "", currentPassword, newPassword)
      .then(() => {
        logout();
      })
      .catch((error) => {
        console.error("Failed to update password:", error);
      });
  };

  const passwordStepsContent = [
    <InputField
      name="currentPassword"
      label="Current Password"
      type="password"
      required
      value={currentPassword}
      onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
        setCurrentPassword(e.target.value)
      }
    />,
    <InputField
      name="newPassword"
      label="New Password"
      type="password"
      required
      value={newPassword}
      onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
        setNewPassword(e.target.value)
      }
    />,
    <div className="space-y-10">
      <InputField
        name="confirmPassword"
        label="Confirm New Password"
        type="password"
        required
        value={confirmPassword}
        onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
          setConfirmPassword(e.target.value)
        }
      />
      <Button onClick={handleUpdatePassword}>Update Password</Button>
    </div>,
  ];

  return (
    <div className="flex justify-center items-center">
      <Tabs defaultValue="account" className="w-[80%]">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="account">Account</TabsTrigger>
          <TabsTrigger value="password">Password</TabsTrigger>
        </TabsList>
        <TabsContent value="account">
          <Card>
            <CardHeader>
              <CardTitle>Account</CardTitle>
              <CardDescription>
                Make changes to your account here. Click save when you're done.
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-2">
              <div className="space-y-1">
                <InputField
                  label="Names"
                  name="name"
                  value={user?.firstName + " " + user?.lastName}
                  readOnly
                />
              </div>
              <div className="space-y-1">
                <InputField
                  label="Email"
                  name="email"
                  value={user?.email}
                  readOnly
                />
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="password">
          <Card>
            <CardHeader>
              <CardTitle>Password</CardTitle>
              <CardDescription>
                Change your password here. After saving, you'll be logged out.
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-2">
              <Steps stepsContent={passwordStepsContent} />
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}
