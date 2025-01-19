import React, { useState } from "react";
import {
  Dialog,
  DialogPanel,
  Transition,
  TransitionChild,
} from "@headlessui/react";
import { navigation } from "../../constants";
import { Link, Outlet, useLocation } from "react-router-dom";
import {
  ChevronLeftIcon,
  ArrowRightStartOnRectangleIcon,
  Bars3Icon,
  XMarkIcon,
  BellAlertIcon,
} from "@heroicons/react/20/solid";
import { Avatar, Button } from "@mantine/core";
import Logo from "../Logo";
import useAuth from "../../hooks/useAuth";
import NotificationModal from "../modals/notification/NotificationModal";
import useNotification from "../../hooks/useNotification";

function classNames(...classes: string[]) {
  return classes.filter(Boolean).join(" ");
}

const DashboardLayout = () => {
  const location = useLocation();
  const { user, logout, loggingOut } = useAuth();
  const { notifications } = useNotification();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [isNotificationOpen, setIsNotificationOpen] = useState(false);

  const unreadNotification = notifications.filter(notification => {
    return !notification.read;
 })

  const toggleCollapse = () => {
    setIsCollapsed(!isCollapsed);
  };

  const openNotifications = () => {
    setIsNotificationOpen(true);
  };

  const closeNotifications = () => {
    setIsNotificationOpen(false);
  };

  return (
    <>
      <div>
        <Transition show={sidebarOpen}>
          <Dialog className="relative z-50 lg:hidden" onClose={setSidebarOpen}>
            <TransitionChild
              enter="transition-opacity ease-linear duration-300"
              enterFrom="opacity-0"
              enterTo="opacity-100"
              leave="transition-opacity ease-linear duration-300"
              leaveFrom="opacity-100"
              leaveTo="opacity-0"
            >
              <div className="fixed inset-0 bg-gray-900/80" />
            </TransitionChild>

            <div className="fixed inset-0 flex">
              <TransitionChild
                enter="transition ease-in-out duration-300 transform"
                enterFrom="-translate-x-full"
                enterTo="translate-x-0"
                leave="transition ease-in-out duration-300 transform"
                leaveFrom="translate-x-0"
                leaveTo="-translate-x-full"
              >
                <DialogPanel className="relative mr-16 flex w-full max-w-xs flex-1">
                  <TransitionChild
                    enter="ease-in-out duration-300"
                    enterFrom="opacity-0"
                    enterTo="opacity-100"
                    leave="ease-in-out duration-300"
                    leaveFrom="opacity-100"
                    leaveTo="opacity-0"
                  >
                    <div className="absolute left-full top-0 flex w-16 justify-center pt-5">
                      <button
                        type="button"
                        className="-m-2.5 p-2.5"
                        onClick={() => setSidebarOpen(false)}
                      >
                        <span className="sr-only">Close sidebar</span>
                        <XMarkIcon
                          className="h-6 w-6 text-white"
                          aria-hidden="true"
                        />
                      </button>
                    </div>
                  </TransitionChild>
                  {/* Sidebar component */}
                  <div className="flex grow flex-col gap-y-5 overflow-y-auto space-y-3 bg-gray-900 px-6 pb-4 ring-1 ring-white/10">
                    <div className="mt-4">
                      <Logo isCollapsed={isCollapsed} />
                    </div>
                    <nav className="flex flex-1 flex-col">
                      <ul role="list" className="flex flex-1 flex-col gap-y-7">
                        <li>
                          <ul role="list" className="-mx-2 space-y-1">
                            {navigation.map((item) => (
                              <li key={item.name}>
                                <Link
                                  to={item.path}
                                  className={classNames(
                                    location.pathname === item.path
                                      ? "bg-gray-800 text-white"
                                      : "text-gray-400 hover:bg-gray-800 hover:text-white",
                                    "group flex gap-x-3 rounded-md p-2 text-sm font-semibold leading-6"
                                  )}
                                >
                                  <item.icon
                                    className="h-6 w-6 shrink-0"
                                    aria-hidden="true"
                                  />
                                  {item.name}
                                </Link>
                              </li>
                            ))}
                          </ul>
                        </li>
                        <li className="mt-auto">
                          <Button variant="outline" color="red">
                            <span>Logout</span>
                            <ArrowRightStartOnRectangleIcon className="w-4 ml-2" />
                          </Button>
                        </li>
                      </ul>
                    </nav>
                  </div>
                </DialogPanel>
              </TransitionChild>
            </div>
          </Dialog>
        </Transition>
        <div
          className={classNames(
            isCollapsed ? "lg:w-16" : "lg:w-72",
            "hidden lg:fixed lg:inset-y-0 lg:z-50 lg:flex lg:flex-col transition-width duration-300"
          )}
        >
          <div className="flex grow flex-col gap-y-5 overflow-y-auto bg-gray-900 px-4 pb-6">
            <div className="flex items-center justify-between h-16">
              <Logo isCollapsed={isCollapsed} />
              <button
                onClick={toggleCollapse}
                className={`text-white bg-gray-800 rounded-md p-2 mx-4 z-40 fixed  w-10 ${
                  isCollapsed ? "inset-x-8" : "inset-x-60"
                }`}
                aria-label="Toggle sidebar"
              >
                <ChevronLeftIcon
                  className={classNames(
                    "h-6 w-6 transition-transform duration-300",
                    isCollapsed ? "rotate-180" : "rotate-0"
                  )}
                  aria-hidden="true"
                />
              </button>
            </div>
            <nav className="flex flex-1 flex-col">
              <ul role="list" className="flex flex-1 flex-col gap-y-7 pt-4">
                <li>
                  <ul role="list" className="-mx-2 space-y-1">
                    {navigation.map((item, idx) => (
                      <li key={idx}>
                        <Link
                          to={item.path}
                          className={classNames(
                            location.pathname === item.path
                              ? "bg-gray-800 text-white"
                              : "text-gray-400 hover:bg-gray-800 hover:text-white",
                            "group flex items-center gap-x-3 rounded-md p-2 text-sm font-semibold leading-6"
                          )}
                        >
                          <item.icon
                            className="h-6 w-6 shrink-0"
                            aria-hidden="true"
                          />
                          {!isCollapsed && <span>{item.name}</span>}
                        </Link>
                      </li>
                    ))}
                  </ul>
                </li>
                <li className="mt-auto">
                  <Button
                    onClick={logout}
                    loading={loggingOut}
                    variant="outline"
                    color="red"
                    size={isCollapsed ? "xs" : "sm"}
                  >
                    <ArrowRightStartOnRectangleIcon
                      className={classNames(isCollapsed ? "w-2" : "w-4 ml-2")}
                    />
                  </Button>
                </li>
              </ul>
            </nav>
          </div>
        </div>

        <div className={classNames(isCollapsed ? "lg:pl-20" : "lg:pl-72")}>
          <div className="sticky top-0 z-40 flex h-16 shrink-0 items-center gap-x-4 border-b border-gray-200 bg-gray-50 px-4 shadow-sm sm:gap-x-6 sm:px-6 lg:px-8">
            <button
              type="button"
              className="-m-2.5 p-2.5 text-gray-700 lg:hidden"
              onClick={() => setSidebarOpen(true)}
            >
              <span className="sr-only">Open sidebar</span>
              <Bars3Icon className="h-6 w-6" aria-hidden="true" />
            </button>

            <div className="flex justify-end flex-1 gap-x-4 self-stretch lg:gap-x-6">
              <div className="flex items-center gap-x-4 lg:gap-x-6">
                <div className="relative flex items-center gap-x-2">
                  <div
                    className="relative cursor-pointer"
                    onClick={openNotifications}
                  >
                    {unreadNotification?.length !== 0 && (
                      <div className="absolute -top-2 -right-2 h-5 w-5 p-1 bg-red-500 rounded-full flex items-center justify-center">
                        <p className="text-xs text-white font-semibold">
                          {unreadNotification?.length}
                        </p>
                      </div>
                    )}
                    <BellAlertIcon
                      width={30}
                      height={30}
                      className="text-gray-800"
                    />
                  </div>

                  <Link to="/dashboard/profile">
                    <div className="-m-1.5 flex items-center p-1.5">
                      <span className="sr-only">Open user menu</span>
                      <Avatar />
                      <span className="flex flex-col">
                        <span
                          className="ml-4 text-sm font-semibold text-gray-900 md:text-base"
                          aria-hidden="true"
                        >
                          {user?.firstName} {user?.lastName}
                        </span>
                        <span
                          className="ml-4 text-xs md:text-sm font-semibold text-gray-500"
                          aria-hidden="true"
                        >
                          {user?.email}
                        </span>
                      </span>
                    </div>
                  </Link>
                </div>
              </div>
            </div>

            <div
              className="h-6 w-px bg-gray-900/10 hidden"
              aria-hidden="true"
            />
          </div>
          <main className="py-6  h-screen">
            <div className="px-4 sm:px-6 lg:px-8">
              <Outlet />
            </div>
          </main>
          <NotificationModal
            isOpen={isNotificationOpen}
            closeModal={closeNotifications}
          />
        </div>
      </div>
    </>
  );
};

export default DashboardLayout;
