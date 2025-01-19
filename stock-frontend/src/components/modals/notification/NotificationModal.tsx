import React, { useState } from 'react';
import { Dialog, DialogPanel, Transition, TransitionChild } from '@headlessui/react';
import { XMarkIcon } from '@heroicons/react/20/solid';
import useNotification from '../../../hooks/useNotification';

interface NotificationModalProps {
  isOpen: boolean;
  closeModal: () => void;
}

const NotificationModal: React.FC<NotificationModalProps> = ({ isOpen, closeModal }) => {
  const { notifications, isLoading, error, markAsRead } = useNotification();
  const [filter, setFilter] = useState<'all' | 'unread' | 'read'>('unread');
  console.log("Here is the notification: ", notifications)
  const filteredNotifications = notifications?.filter(notification => {
    if (filter === 'unread') return !notification.read;
    if (filter === 'read') return notification.read;
    return true;
  });



  return (
    <Transition show={isOpen}>
      <Dialog as="div" className="relative z-50" onClose={closeModal}>
        <TransitionChild
          enter="transition-opacity ease-linear duration-300"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="transition-opacity ease-linear duration-300"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-black bg-opacity-30" />
        </TransitionChild>

        <div className="fixed inset-0 flex items-center justify-center lg:justify-end lg:-top-[30rem] lg:right-52 p-4">
          <TransitionChild
            enter="transition ease-in-out duration-300 transform"
            enterFrom="scale-95"
            enterTo="scale-100"
            leave="transition ease-in-out duration-300 transform"
            leaveFrom="scale-100"
            leaveTo="scale-95"
          >
            <DialogPanel className="w-full max-w-md transform rounded-lg bg-white p-6 text-left shadow-xl transition-all">
              <div className="flex justify-between items-center">
                <h3 className="text-lg font-medium leading-6 text-gray-900">Notifications</h3>
                <button className="text-gray-400 hover:text-gray-600" onClick={closeModal}>
                  <XMarkIcon className="h-6 w-6" />
                </button>
              </div>

              <div className="mt-4 flex justify-between">
                <button
                  className={`text-sm font-medium ${filter === 'all' ? 'text-blue-600' : 'text-gray-500'}`}
                  onClick={() => setFilter('all')}
                >
                  All
                </button>
                <button
                  className={`text-sm font-medium ${filter === 'unread' ? 'text-blue-600' : 'text-gray-500'}`}
                  onClick={() => setFilter('unread')}
                >
                  Unread
                </button>
                <button
                  className={`text-sm font-medium ${filter === 'read' ? 'text-blue-600' : 'text-gray-500'}`}
                  onClick={() => setFilter('read')}
                >
                  Read
                </button>
              </div>

              <div className="mt-4 max-h-96 overflow-y-auto">
                {isLoading ? (
                  <p className="text-sm text-gray-500">Loading notifications...</p>
                ) : error ? (
                  <p className="text-sm text-red-500">Failed to load notifications.</p>
                ) : filteredNotifications && filteredNotifications.length > 0 ? (
                  <ul className="space-y-4">
                    {filteredNotifications.map((notification, index) => (
                      <li
                        key={index}
                        className={`rounded p-3 ${notification.read ? 'bg-gray-100' : 'bg-blue-100'}`}
                        onClick={() => markAsRead(notification.id)} 
                      >
                        <p className="text-sm font-semibold text-gray-900">{notification.message}</p>
                        <p className="text-sm text-gray-500">{notification.category}</p>
                        <p className="text-sm text-gray-500">Name: {notification.name}</p>
                        <p className="text-sm text-gray-500">Quantity: {notification.quantity}</p>
                        <p className="text-xs text-right text-gray-500">{notification.read ? 'Read' : 'Unread'}</p>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p className="text-sm text-gray-500">No notifications found.</p>
                )}
              </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </Dialog>
    </Transition>
  );
};

export default NotificationModal;
