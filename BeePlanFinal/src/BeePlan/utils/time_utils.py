"""
Time utility functions
"""
from datetime import time
from typing import List, Tuple
from models.schedule import DayOfWeek, TimeSlot


class TimeUtils:
    """Utility functions for time operations"""
    
    # Standard time slots for the schedule (starting at 9:20)
    TIME_SLOTS = [
        (time(9, 20), time(10, 10)),   # Slot 0
        (time(10, 20), time(11, 10)),  # Slot 1
        (time(11, 20), time(12, 10)),  # Slot 2
        (time(12, 20), time(13, 10)),  # Slot 3
        (time(13, 20), time(14, 10)),  # Slot 4
        (time(14, 20), time(15, 10)),  # Slot 5
        (time(15, 20), time(16, 10)),  # Slot 6
        (time(16, 20), time(17, 10)),  # Slot 7
    ]
    
    @staticmethod
    def get_time_slot_index(start_time: time) -> int:
        """Get index of time slot for a given start time"""
        for idx, (slot_start, _) in enumerate(TimeUtils.TIME_SLOTS):
            if slot_start == start_time:
                return idx
        # If exact match not found, find closest slot
        for idx, (slot_start, _) in enumerate(TimeUtils.TIME_SLOTS):
            if slot_start <= start_time:
                # Check if within this slot's range
                if idx + 1 < len(TimeUtils.TIME_SLOTS):
                    next_start = TimeUtils.TIME_SLOTS[idx + 1][0]
                    if start_time < next_start:
                        return idx
        return 0  # Default to first slot
    
    @staticmethod
    def get_consecutive_slots(hours: int, start_slot: int) -> List[Tuple[time, time]]:
        """Get consecutive time slots for given hours"""
        slots = []
        for i in range(hours):
            if start_slot + i < len(TimeUtils.TIME_SLOTS):
                slots.append(TimeUtils.TIME_SLOTS[start_slot + i])
        return slots
    
    @staticmethod
    def is_friday_exam_block(start_time: time, end_time: time) -> bool:
        """Check if time slot is in Friday exam block (13:20-15:10)"""
        exam_start = time(13, 20)
        exam_end = time(15, 10)
        return not (end_time <= exam_start or start_time >= exam_end)
    
    @staticmethod
    def get_all_days() -> List[DayOfWeek]:
        """Get all weekdays"""
        return [
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        ]

