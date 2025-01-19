import React from "react";


export default function AuthLayout({ children } : { children : React.ReactNode}){
    return (
        <div className="flex min-h-screen flex-1">
            <div className="flex flex-1 flex-col justify-center px-4 py-12 sm:px-6 lg:flex-none lg:px-20 xl:px-24">
            <div className="flex items-center justify-center">
                <img 
                  src="/logo.png"
                />
            </div>
                {children}
            </div>

            <div className="relative hidden w-0 flex-1 lg:block">
                <img
                  src="/stock.png"
                  className="absolute inset-0 h-full w-full object-cover"
                />
            </div>
        </div>
    )
}