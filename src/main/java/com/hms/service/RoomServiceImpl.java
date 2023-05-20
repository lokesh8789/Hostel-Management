package com.hms.service;

import com.hms.entity.Room;
import com.hms.repo.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService{

    @Autowired
    private RoomRepo roomRepo;

    @Override
    public List<Room> findByHostelName(String hostelName) {
        return this.roomRepo.findByHostelName(hostelName);
    }

    @Override
    public Room updateRoomBYId(int roomNo, int isEmpty,String hostelName) {
        Room room = roomRepo.findByRoomNoAndHostelName(roomNo,hostelName);
        room.setIsEmpty(isEmpty);
        Room save = roomRepo.save(room);
        return save;
    }

    @Override
    public Room findByRoomAndHostel(int roomNo, String hostelName) {
        Room room = this.roomRepo.findByRoomNoAndHostelName(roomNo, hostelName);
        return room;
    }
}
