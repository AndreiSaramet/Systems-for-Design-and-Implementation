package ro.ubb.opera.client.service;

import ro.ubb.opera.client.tcp.TcpClient;
import ro.ubb.opera.common.domain.Venue;
import ro.ubb.opera.common.service.Service;
import ro.ubb.opera.common.service.VenueService;
import ro.ubb.opera.common.service.exceptions.ServiceException;
import ro.ubb.opera.common.tcp.Message;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class VenueServiceClient implements VenueService {
    private final ExecutorService executorService;

    private final TcpClient tcpClient;

    public VenueServiceClient(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<Venue> addVenue(Integer seatsNo, Integer floor) {
        return this.executorService.submit(() -> {
            Message request = new Message(VenueService.ADD_VENUE, seatsNo.toString() + Service.TOKENS_DELIMITER + floor.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return VenueService.decodeVenue(response.getBody());
        });
    }

    @Override
    public Future<Venue> findVenueById(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(VenueService.FIND_VENUE_BY_ID, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return VenueService.decodeVenue(response.getBody());
        });
    }

    @Override
    public Future<Set<Venue>> findVenuesBySeatsNo(Integer seatsNo) {
        return this.executorService.submit(() -> {
            Message request = new Message(VenueService.FIND_VENUES_BY_SEATS_NO, seatsNo.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeVenueSet(response.getBody());
        });
    }

    @Override
    public Future<Set<Venue>> findVenuesByFloor(Integer floor) {
        return this.executorService.submit(() -> {
            Message request = new Message(VenueService.FIND_VENUES_BY_FLOOR, floor.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeVenueSet(response.getBody());
        });
    }

    @Override
    public Future<Set<Venue>> getAllVenues() {
        return this.executorService.submit(() -> {
            Message request = new Message(VenueService.GET_ALL_VENUES, "");

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeVenueSet(response.getBody());
        });
    }

    @Override
    public Future<Venue> updateVenue(Integer id, Integer seatsNo, Integer floor) {
        return this.executorService.submit(() -> {
            Message request = new Message(VenueService.UPDATE_VENUE, id.toString() + Service.TOKENS_DELIMITER + seatsNo.toString() + Service.TOKENS_DELIMITER + floor.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return VenueService.decodeVenue(response.getBody());
        });
    }

    @Override
    public Future<Venue> deleteVenue(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(VenueService.DELETE_VENUE, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return VenueService.decodeVenue(response.getBody());
        });
    }

    private Set<Venue> decodeVenueSet(String encodedVenueSet) {
        Set<String> tokens = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(encodedVenueSet, Service.ENTITIES_DELIMITER);
        while (stringTokenizer.hasMoreTokens()) {
            tokens.add(stringTokenizer.nextToken());
        }
        return tokens.stream().map(VenueService::decodeVenue).collect(Collectors.toSet());
    }
}
