import React, { useState } from 'react';
import { CheckCircle, Award, Plus, Calendar, LogOut, User, ListTodo, ClipboardCheck, Gift } from 'lucide-react';

interface TaskSubmission {
  id: number;
  childName: string;
  taskTitle: string;
  submittedDate: string;
  status: 'pending' | 'approved' | 'rejected';
}

interface Achievement {
  id: number;
  title: string;
  description: string;
  reward: string;
}

type ViewMode = 'show-tasks' | 'approval-center' | 'assign-task' | 'achievements' | 'schedule';

export function ParentDashboard() {
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [activeView, setActiveView] = useState<ViewMode>('assign-task');
  
  const [taskFormData, setTaskFormData] = useState({
    title: '',
    description: '',
    dueDate: '',
    rewardPoints: ''
  });

  const [achievementFormData, setAchievementFormData] = useState({
    title: '',
    description: '',
    reward: ''
  });

  const [submissions, setSubmissions] = useState<TaskSubmission[]>([
    { id: 1, childName: 'Emma', taskTitle: 'Complete Homework', submittedDate: '2024-12-28', status: 'pending' },
    { id: 2, childName: 'Emma', taskTitle: 'Clean Room', submittedDate: '2024-12-28', status: 'pending' },
    { id: 3, childName: 'Liam', taskTitle: 'Practice Piano', submittedDate: '2024-12-27', status: 'pending' },
    { id: 4, childName: 'Emma', taskTitle: 'Read 30 Pages', submittedDate: '2024-12-27', status: 'pending' },
  ]);

  const [achievements, setAchievements] = useState<Achievement[]>([
    { id: 1, title: 'Math Master', description: 'Finish 5 Math tasks', reward: 'Movie Night' },
    { id: 2, title: 'Reading Champion', description: 'Read 3 books this month', reward: 'New Book' },
  ]);

  const dailyProgress = 65;

  const handleApproval = (submissionId: number, approved: boolean) => {
    setSubmissions(submissions.map(sub => 
      sub.id === submissionId 
        ? { ...sub, status: approved ? 'approved' : 'rejected' } 
        : sub
    ));
  };

  const handleTaskFormChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setTaskFormData({ ...taskFormData, [e.target.name]: e.target.value });
  };

  const handleAchievementFormChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setAchievementFormData({ ...achievementFormData, [e.target.name]: e.target.value });
  };

  const handleSubmitTask = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Task assigned:', taskFormData);
    setTaskFormData({ title: '', description: '', dueDate: '', rewardPoints: '' });
  };

  const handleSubmitAchievement = (e: React.FormEvent) => {
    e.preventDefault();
    const newAchievement: Achievement = {
      id: achievements.length + 1,
      title: achievementFormData.title,
      description: achievementFormData.description,
      reward: achievementFormData.reward
    };
    setAchievements([...achievements, newAchievement]);
    setAchievementFormData({ title: '', description: '', reward: '' });
  };

  return (
    <div className="min-h-screen relative overflow-hidden">
      {/* Background with mint green gradients */}
      <div className="absolute inset-0 bg-gradient-to-br from-[#F0FFF4] via-white to-[#F0FFF4]"></div>
      
      {/* Corner gradients - Mint Green */}
      <div className="absolute top-0 left-0 w-96 h-96 bg-[#98FB98] rounded-full opacity-15 blur-3xl"></div>
      <div className="absolute bottom-0 right-0 w-96 h-96 bg-[#98FB98] rounded-full opacity-15 blur-3xl"></div>

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
            <button 
              onClick={() => setActiveView('show-tasks')}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                activeView === 'show-tasks' 
                  ? 'bg-[#98FB98]/20' 
                  : 'hover:bg-[#98FB98]/10'
              }`}
            >
              <ListTodo className="w-5 h-5" style={{ color: '#98FB98' }} />
              <span className="text-[#0056B3] font-medium">Show Tasks</span>
            </button>
            
            <button 
              onClick={() => setActiveView('approval-center')}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                activeView === 'approval-center' 
                  ? 'bg-[#98FB98]/20' 
                  : 'hover:bg-[#98FB98]/10'
              }`}
            >
              <ClipboardCheck className="w-5 h-5" style={{ color: '#98FB98' }} />
              <span className="text-[#0056B3] font-medium">Approval Center</span>
            </button>
            
            <button 
              onClick={() => setActiveView('assign-task')}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                activeView === 'assign-task' 
                  ? 'bg-[#98FB98]/20' 
                  : 'hover:bg-[#98FB98]/10'
              }`}
            >
              <Plus className="w-5 h-5" style={{ color: '#98FB98' }} />
              <span className="text-[#0056B3] font-medium">Assign New Task</span>
            </button>
            
            <button 
              onClick={() => setActiveView('achievements')}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                activeView === 'achievements' 
                  ? 'bg-[#98FB98]/20' 
                  : 'hover:bg-[#98FB98]/10'
              }`}
            >
              <Award className="w-5 h-5" style={{ color: '#98FB98' }} />
              <span className="text-[#0056B3] font-medium">Add Achievements</span>
            </button>
            
            <button 
              onClick={() => setActiveView('schedule')}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                activeView === 'schedule' 
                  ? 'bg-[#98FB98]/20' 
                  : 'hover:bg-[#98FB98]/10'
              }`}
            >
              <Calendar className="w-5 h-5" style={{ color: '#98FB98' }} />
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
                {/* Title */}
                <div className="mb-4">
                  <h3 className="font-bold text-[#0056B3] text-lg">Child's Daily Progress</h3>
                </div>

                {/* Progress Bar */}
                <div className="flex items-center space-x-3">
                  <div className="flex-1 h-[12px] bg-gray-200 rounded-full overflow-hidden">
                    <div 
                      className="h-full bg-[#98FB98] transition-all duration-500"
                      style={{ width: `${dailyProgress}%` }}
                    ></div>
                  </div>
                  <span className="font-bold text-[#0056B3] text-lg">{dailyProgress}% Complete</span>
                </div>
              </div>
            </div>

            {/* Profile Avatar & Dropdown */}
            <div className="relative ml-6">
              <button 
                onClick={() => setShowProfileMenu(!showProfileMenu)}
                className="w-12 h-12 rounded-full bg-gradient-to-br from-[#98FB98] to-[#7FE87F] flex items-center justify-center hover:scale-105 transition-transform duration-200 shadow-lg"
              >
                <User className="w-6 h-6 text-white" />
              </button>

              {/* Dropdown Menu */}
              {showProfileMenu && (
                <div className="absolute right-0 mt-2 w-48 bg-white/90 backdrop-blur-xl border border-white/40 rounded-xl shadow-2xl overflow-hidden">
                  <button className="w-full flex items-center space-x-3 px-4 py-3 hover:bg-[#98FB98]/10 transition-colors">
                    <LogOut className="w-5 h-5 text-[#0056B3]" />
                    <span className="text-[#0056B3] font-medium">Log Out</span>
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>

        {/* Main Dynamic Content Area */}
        <main className="p-6">
          <div className="max-w-4xl mx-auto">
            {/* Show Tasks View */}
            {activeView === 'show-tasks' && (
              <div>
                <h1 className="text-3xl font-bold text-[#0056B3] mb-6">Current Tasks</h1>
                
                <div className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-8">
                  <div className="text-center py-12">
                    <ListTodo className="w-16 h-16 mx-auto mb-4" style={{ color: '#98FB98' }} />
                    <p className="text-[#0056B3] text-lg">Task list view coming soon...</p>
                  </div>
                </div>
              </div>
            )}

            {/* Approval Center View */}
            {activeView === 'approval-center' && (
              <div>
                <h1 className="text-3xl font-bold text-[#0056B3] mb-6">Approval Center</h1>
                
                <div className="space-y-4">
                  {submissions.filter(s => s.status === 'pending').map((submission) => (
                    <div 
                      key={submission.id}
                      className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-6 hover:shadow-xl transition-all duration-300"
                    >
                      <div className="flex items-start justify-between">
                        <div className="flex-1">
                          <div className="flex items-center space-x-2 mb-2">
                            <User className="w-5 h-5 text-[#98FB98]" />
                            <h3 className="font-bold text-lg text-gray-800">
                              {submission.childName}
                            </h3>
                          </div>
                          <p className="text-[#0056B3] font-medium mb-1">
                            {submission.taskTitle}
                          </p>
                          <p className="text-gray-600 text-sm">
                            Submitted: {new Date(submission.submittedDate).toLocaleDateString()}
                          </p>
                        </div>
                        
                        <div className="flex space-x-3 ml-4">
                          <button
                            onClick={() => handleApproval(submission.id, true)}
                            className="px-5 py-2 bg-[#98FB98] text-white rounded-xl font-semibold hover:bg-[#7FE87F] hover:shadow-lg transition-all duration-200"
                          >
                            Approve
                          </button>
                          <button
                            onClick={() => handleApproval(submission.id, false)}
                            className="px-5 py-2 bg-red-500 text-white rounded-xl font-semibold hover:bg-red-600 hover:shadow-lg transition-all duration-200"
                          >
                            Reject
                          </button>
                        </div>
                      </div>
                    </div>
                  ))}
                  
                  {submissions.filter(s => s.status === 'pending').length === 0 && (
                    <div className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-12 text-center">
                      <CheckCircle className="w-16 h-16 mx-auto mb-4" style={{ color: '#98FB98' }} />
                      <p className="text-[#0056B3] text-lg">All caught up! No pending submissions.</p>
                    </div>
                  )}
                </div>
              </div>
            )}

            {/* Assign Task View */}
            {activeView === 'assign-task' && (
              <div>
                <h1 className="text-3xl font-bold text-[#0056B3] mb-6">Assign New Task</h1>
                
                <div className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-8">
                  <form onSubmit={handleSubmitTask} className="space-y-6">
                    <div>
                      <label className="block text-[#0056B3] font-semibold mb-2">Task Title</label>
                      <input
                        type="text"
                        name="title"
                        value={taskFormData.title}
                        onChange={handleTaskFormChange}
                        className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#98FB98] transition-all"
                        placeholder="Enter task title"
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-[#0056B3] font-semibold mb-2">Description</label>
                      <textarea
                        name="description"
                        value={taskFormData.description}
                        onChange={handleTaskFormChange}
                        rows={4}
                        className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#98FB98] transition-all resize-none"
                        placeholder="Enter task description"
                        required
                      />
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-[#0056B3] font-semibold mb-2">Due Date</label>
                        <input
                          type="date"
                          name="dueDate"
                          value={taskFormData.dueDate}
                          onChange={handleTaskFormChange}
                          className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#98FB98] transition-all"
                          required
                        />
                      </div>

                      <div>
                        <label className="block text-[#0056B3] font-semibold mb-2">Reward Points</label>
                        <input
                          type="number"
                          name="rewardPoints"
                          value={taskFormData.rewardPoints}
                          onChange={handleTaskFormChange}
                          className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#98FB98] transition-all"
                          placeholder="0"
                          min="0"
                          required
                        />
                      </div>
                    </div>

                    <button
                      type="submit"
                      className="w-full px-6 py-4 bg-gradient-to-r from-[#98FB98] to-[#7FE87F] text-white rounded-xl font-semibold hover:shadow-lg hover:scale-[1.02] transition-all duration-200"
                    >
                      Assign Task
                    </button>
                  </form>
                </div>
              </div>
            )}

            {/* Add Achievements View */}
            {activeView === 'achievements' && (
              <div>
                <h1 className="text-3xl font-bold text-[#0056B3] mb-6">Achievement System</h1>
                
                {/* Create Achievement Form */}
                <div className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-8 mb-6">
                  <h2 className="text-xl font-bold text-[#0056B3] mb-4">Create New Incentive</h2>
                  <form onSubmit={handleSubmitAchievement} className="space-y-4">
                    <div>
                      <label className="block text-[#0056B3] font-semibold mb-2">Achievement Title</label>
                      <input
                        type="text"
                        name="title"
                        value={achievementFormData.title}
                        onChange={handleAchievementFormChange}
                        className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#98FB98] transition-all"
                        placeholder="e.g., Math Master"
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-[#0056B3] font-semibold mb-2">Description (Goal)</label>
                      <input
                        type="text"
                        name="description"
                        value={achievementFormData.description}
                        onChange={handleAchievementFormChange}
                        className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#98FB98] transition-all"
                        placeholder="e.g., Finish 5 Math tasks"
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-[#0056B3] font-semibold mb-2">Reward</label>
                      <input
                        type="text"
                        name="reward"
                        value={achievementFormData.reward}
                        onChange={handleAchievementFormChange}
                        className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#98FB98] transition-all"
                        placeholder="e.g., Movie Night"
                        required
                      />
                    </div>

                    <button
                      type="submit"
                      className="w-full px-6 py-4 bg-gradient-to-r from-[#98FB98] to-[#7FE87F] text-white rounded-xl font-semibold hover:shadow-lg hover:scale-[1.02] transition-all duration-200"
                    >
                      Create Achievement
                    </button>
                  </form>
                </div>

                {/* Existing Achievements */}
                <h2 className="text-xl font-bold text-[#0056B3] mb-4">Active Achievements</h2>
                <div className="space-y-4">
                  {achievements.map((achievement) => (
                    <div 
                      key={achievement.id}
                      className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-6"
                    >
                      <div className="flex items-start space-x-4">
                        <div className="w-12 h-12 rounded-full bg-[#98FB98]/20 flex items-center justify-center flex-shrink-0">
                          <Gift className="w-6 h-6" style={{ color: '#98FB98' }} />
                        </div>
                        <div className="flex-1">
                          <h3 className="font-bold text-lg text-gray-800 mb-1">
                            {achievement.title}
                          </h3>
                          <p className="text-[#0056B3] mb-2">
                            Goal: {achievement.description}
                          </p>
                          <div className="flex items-center space-x-2">
                            <Award className="w-5 h-5" style={{ color: '#98FB98' }} />
                            <span className="text-gray-700 font-medium">
                              Reward: {achievement.reward}
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Schedule View */}
            {activeView === 'schedule' && (
              <div>
                <h1 className="text-3xl font-bold text-[#0056B3] mb-6">Family Schedule</h1>
                
                <div className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-8">
                  <div className="text-center py-12">
                    <Calendar className="w-16 h-16 mx-auto mb-4" style={{ color: '#98FB98' }} />
                    <p className="text-[#0056B3] text-lg">Schedule view coming soon...</p>
                  </div>
                </div>
              </div>
            )}
          </div>
        </main>
      </div>
    </div>
  );
}
