package ro.ubb.opera.client.service;

import ro.ubb.opera.client.tcp.TcpClient;
import ro.ubb.opera.common.domain.Rehearsal;
import ro.ubb.opera.common.service.RehearsalService;
import ro.ubb.opera.common.service.Service;
import ro.ubb.opera.common.service.exceptions.ServiceException;
import ro.ubb.opera.common.tcp.Message;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class RehearsalServiceClient implements RehearsalService {
    private final ExecutorService executorService;

    private final TcpClient tcpClient;

    public RehearsalServiceClient(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<Rehearsal> addRehearsal(Integer orchestraId, Integer venueId) {
        return this.executorService.submit(() -> {
            Message request = new Message(RehearsalService.ADD_REHEARSAL, orchestraId.toString() + Service.TOKENS_DELIMITER + venueId.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return RehearsalService.decodeRehearsal(response.getBody());
        });
    }

    @Override
    public Future<Rehearsal> findRehearsalById(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(RehearsalService.FIND_REHEARSAL_BY_ID, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return RehearsalService.decodeRehearsal(response.getBody());
        });
    }

    @Override
    public Future<Set<Rehearsal>> findRehearsalsByOrchestra(Integer orchestraId) {
        return this.executorService.submit(() -> {
            Message request = new Message(RehearsalService.FIND_REHEARSALS_BY_ORCHESTRA, orchestraId.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeRehearsalSet(response.getBody());
        });
    }

    @Override
    public Future<Set<Rehearsal>> findRehearsalsByVenue(Integer venueId) {
        return this.executorService.submit(() -> {
            Message request = new Message(RehearsalService.FIND_REHEARSALS_BY_VENUE, venueId.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeRehearsalSet(response.getBody());
        });
    }

    @Override
    public Future<Set<Rehearsal>> getAllRehearsals() {
        return this.executorService.submit(() -> {
            Message request = new Message(RehearsalService.GET_ALL_REHEARSALS, "");

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeRehearsalSet(response.getBody());
        });
    }

    @Override
    public Future<Rehearsal> updateRehearsal(Integer id, Integer orchestraId, Integer venueId) {
        return this.executorService.submit(() -> {
            Message request = new Message(RehearsalService.UPDATE_REHEARSAL, id.toString() + Service.TOKENS_DELIMITER + orchestraId.toString() + Service.TOKENS_DELIMITER + venueId.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return RehearsalService.decodeRehearsal(response.getBody());
        });
    }

    @Override
    public Future<Rehearsal> deleteRehearsal(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(RehearsalService.DELETE_REHEARSAL, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return RehearsalService.decodeRehearsal(response.getBody());
        });
    }

    private Set<Rehearsal> decodeRehearsalSet(String encodedRehearsalSet) {
        Set<String> tokens = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(encodedRehearsalSet, Service.ENTITIES_DELIMITER);
        while (stringTokenizer.hasMoreTokens()) {
            tokens.add(stringTokenizer.nextToken());
        }
        return tokens.stream().map(RehearsalService::decodeRehearsal).collect(Collectors.toSet());
    }
}
