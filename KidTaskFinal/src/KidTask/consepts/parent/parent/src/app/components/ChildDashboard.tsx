import React, { useState } from 'react';
import { CheckCircle, Trophy, Calendar, LogOut, Settings, User } from 'lucide-react';

interface Task {
  id: number;
  title: string;
  points: number;
  completed: boolean;
}

export function ChildDashboard() {
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [tasks, setTasks] = useState<Task[]>([
    { id: 1, title: 'Complete Math Homework', points: 50, completed: false },
    { id: 2, title: 'Clean Your Room', points: 30, completed: false },
    { id: 3, title: 'Read 20 Pages', points: 40, completed: false },
    { id: 4, title: 'Practice Piano for 30 Minutes', points: 45, completed: false },
    { id: 5, title: 'Help with Dinner Preparation', points: 35, completed: false },
  ]);

  const handleCompleteTask = (taskId: number) => {
    setTasks(tasks.map(task => 
      task.id === taskId ? { ...task, completed: true } : task
    ));
  };

  const progressPercentage = 33;
  const level = 5;
  const achievements = { current: 12, total: 20 };

  return (
    <div className="min-h-screen relative overflow-hidden">
      {/* Background with radial gradients */}
      <div className="absolute inset-0 bg-gradient-to-br from-[#FFF5EE] via-white to-[#FFF5EE]"></div>
      
      {/* Corner gradients */}
      <div className="absolute top-0 left-0 w-96 h-96 bg-[#FFDAB9] rounded-full opacity-20 blur-3xl"></div>
      <div className="absolute bottom-0 right-0 w-96 h-96 bg-[#FFDAB9] rounded-full opacity-20 blur-3xl"></div>

      {/* Fixed Left Sidebar */}
      <aside className="fixed left-0 top-0 h-screen w-64 bg-white/20 backdrop-blur-[15px] border-r border-white/30 shadow-lg z-40">
        <div className="p-6">
          {/* KidTask Logo */}
          <div className="flex items-center space-x-2 mb-12">
            <svg width="32" height="32" viewBox="0 0 48 48" fill="none">
              <rect x="10" y="8" width="28" height="36" rx="2" fill="#0056B3" stroke="#0056B3" strokeWidth="2"/>
              <circle cx="16" cy="8" r="2" fill="#0056B3"/>
              <circle cx="24" cy="8" r="2" fill="#0056B3"/>
              <circle cx="32" cy="8" r="2" fill="#0056B3"/>
              <line x1="15" y1="18" x2="33" y2="18" stroke="white" strokeWidth="1.5"/>
              <line x1="15" y1="24" x2="33" y2="24" stroke="white" strokeWidth="1.5"/>
              <line x1="15" y1="30" x2="28" y2="30" stroke="white" strokeWidth="1.5"/>
            </svg>
            <span className="font-bold text-xl text-[#0056B3]">KidTask</span>
          </div>

          {/* Navigation Items */}
          <nav className="space-y-2">
            <button className="w-full flex items-center space-x-3 px-4 py-3 rounded-xl bg-[#0056B3]/10 hover:bg-[#0056B3]/20 transition-all duration-200">
              <CheckCircle className="w-5 h-5 text-[#0056B3]" />
              <span className="text-[#0056B3] font-medium">Tasks</span>
            </button>
            <button className="w-full flex items-center space-x-3 px-4 py-3 rounded-xl hover:bg-[#0056B3]/10 transition-all duration-200">
              <Trophy className="w-5 h-5 text-[#0056B3]" />
              <span className="text-[#0056B3] font-medium">Achievements</span>
            </button>
            <button className="w-full flex items-center space-x-3 px-4 py-3 rounded-xl hover:bg-[#0056B3]/10 transition-all duration-200">
              <Calendar className="w-5 h-5 text-[#0056B3]" />
              <span className="text-[#0056B3] font-medium">Schedule</span>
            </button>
          </nav>
        </div>
      </aside>

      {/* Main Content Area */}
      <div className="ml-64 relative z-10">
        {/* Top Header Bar */}
        <header className="sticky top-0 z-30 p-6">
          <div className="flex items-center justify-between">
            {/* Center Progress Card */}
            <div className="flex-1 max-w-2xl mx-auto">
              <div className="bg-white/30 backdrop-blur-xl border border-white/40 rounded-2xl shadow-xl p-6">
                {/* Top Row: Level and Achievements */}
                <div className="flex items-center justify-between mb-4">
                  <div className="flex items-center space-x-2">
                    <span className="font-bold text-[#0056B3]">Level:</span>
                    <span className="font-bold text-[#0056B3] text-xl">{level}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Trophy className="w-5 h-5 text-[#FFD700]" />
                    <span className="font-bold text-[#0056B3]">Achievements:</span>
                    <span className="font-bold text-[#0056B3] text-xl">
                      {achievements.current}/{achievements.total}
                    </span>
                  </div>
                </div>

                {/* Progress Bar */}
                <div className="flex items-center space-x-3">
                  <div className="flex-1 h-[10px] bg-gray-200 rounded-full overflow-hidden">
                    <div 
                      className="h-full bg-[#FFD700] transition-all duration-500"
                      style={{ width: `${progressPercentage}%` }}
                    ></div>
                  </div>
                  <span className="font-bold text-[#0056B3] text-sm">{progressPercentage}%</span>
                </div>
              </div>
            </div>

            {/* Profile Avatar & Dropdown */}
            <div className="relative ml-6">
              <button 
                onClick={() => setShowProfileMenu(!showProfileMenu)}
                className="w-12 h-12 rounded-full bg-gradient-to-br from-[#0056B3] to-[#0077D4] flex items-center justify-center hover:scale-105 transition-transform duration-200 shadow-lg"
              >
                <User className="w-6 h-6 text-white" />
              </button>

              {/* Dropdown Menu */}
              {showProfileMenu && (
                <div className="absolute right-0 mt-2 w-56 bg-white/90 backdrop-blur-xl border border-white/40 rounded-xl shadow-2xl overflow-hidden">
                  <button className="w-full flex items-center space-x-3 px-4 py-3 hover:bg-[#0056B3]/10 transition-colors">
                    <Settings className="w-5 h-5 text-[#0056B3]" />
                    <span className="text-[#0056B3] font-medium">Profile Settings</span>
                  </button>
                  <button className="w-full flex items-center space-x-3 px-4 py-3 hover:bg-[#0056B3]/10 transition-colors border-t border-gray-200">
                    <LogOut className="w-5 h-5 text-[#0056B3]" />
                    <span className="text-[#0056B3] font-medium">Logout</span>
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>

        {/* Main Task List Area */}
        <main className="p-6">
          <div className="max-w-4xl mx-auto">
            <h1 className="text-3xl font-bold text-[#0056B3] mb-6">My Tasks</h1>
            
            <div className="space-y-4">
              {tasks.map((task) => (
                <div 
                  key={task.id}
                  className={`bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-6 transition-all duration-300 ${
                    task.completed ? 'opacity-60' : 'hover:shadow-xl hover:scale-[1.02]'
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <div className="flex-1">
                      <h3 className={`font-bold text-lg mb-2 ${
                        task.completed ? 'line-through text-gray-500' : 'text-gray-800'
                      }`}>
                        {task.title}
                      </h3>
                      <div className="flex items-center space-x-2">
                        <div className="px-3 py-1 bg-[#FFD700]/20 rounded-full">
                          <span className="text-[#0056B3] font-semibold text-sm">
                            {task.points} points
                          </span>
                        </div>
                      </div>
                    </div>
                    
                    {!task.completed ? (
                      <button 
                        onClick={() => handleCompleteTask(task.id)}
                        className="px-6 py-3 bg-gradient-to-r from-[#0056B3] to-[#0077D4] text-white rounded-xl font-semibold hover:shadow-lg hover:scale-105 transition-all duration-200"
                      >
                        Complete
                      </button>
                    ) : (
                      <div className="flex items-center space-x-2 text-green-600">
                        <CheckCircle className="w-6 h-6" />
                        <span className="font-semibold">Completed!</span>
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}
