package ro.ubb.opera.client.service;

import ro.ubb.opera.client.tcp.TcpClient;
import ro.ubb.opera.common.domain.Composer;
import ro.ubb.opera.common.service.ComposerService;
import ro.ubb.opera.common.service.Service;
import ro.ubb.opera.common.service.exceptions.ServiceException;
import ro.ubb.opera.common.tcp.Message;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ComposerServiceClient implements ComposerService {
    private final ExecutorService executorService;

    private final TcpClient tcpClient;

    public ComposerServiceClient(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<Composer> addComposer(String name, String nationality, String musicalPeriod) {
        return this.executorService.submit(() -> {
            Message request = new Message(ComposerService.ADD_COMPOSER, name + TOKENS_DELIMITER + nationality + TOKENS_DELIMITER + musicalPeriod);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return ComposerService.decodeComposer(response.getBody());
        });
    }

    @Override
    public Future<Composer> findComposerById(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(ComposerService.FIND_COMPOSER_BY_ID, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return ComposerService.decodeComposer(response.getBody());
        });
    }

    @Override
    public Future<Composer> findComposerByName(String name) {
        return this.executorService.submit(() -> {
            Message request = new Message(ComposerService.FIND_COMPOSER_BY_NAME, name);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            if ("".equals(response.getBody())) {
                return null;
            }

            return ComposerService.decodeComposer(response.getBody());
        });
    }

    @Override
    public Future<Set<Composer>> findComposersByNationality(String nationality) {
        return this.executorService.submit(() -> {
            Message request = new Message(ComposerService.FIND_COMPOSERS_BY_NATIONALITY, nationality);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeComposerSet(response.getBody());
        });
    }

    @Override
    public Future<Set<Composer>> findComposersByMusicalPeriod(String musicalPeriod) {
        return this.executorService.submit(() -> {
            Message request = new Message(ComposerService.FIND_COMPOSERS_BY_MUSICAL_PERIOD, musicalPeriod);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeComposerSet(response.getBody());
        });
    }

    @Override
    public Future<Set<Composer>> getAllComposers() {
        return this.executorService.submit(() -> {
            Message request = new Message(ComposerService.GET_ALL_COMPOSERS, "");

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeComposerSet(response.getBody());
        });
    }

    @Override
    public Future<Composer> updateComposer(Integer id, String name, String nationality, String musicalPeriod) {
        return this.executorService.submit(() -> {
            Message request = new Message(ComposerService.UPDATE_COMPOSER, id.toString() + TOKENS_DELIMITER + name + TOKENS_DELIMITER + nationality + TOKENS_DELIMITER + musicalPeriod);

            Message response = this.tcpClient.sendAndReceive(request);

            return ComposerService.decodeComposer(response.getBody());
        });
    }

    @Override
    public Future<Composer> deleteComposer(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(ComposerService.DELETE_COMPOSER, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return ComposerService.decodeComposer(response.getBody());
        });
    }

    private Set<Composer> decodeComposerSet(String encodedComposerSet) {
        Set<String> tokens = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(encodedComposerSet, Service.ENTITIES_DELIMITER);
        while (stringTokenizer.hasMoreTokens()) {
            tokens.add(stringTokenizer.nextToken());
        }
        return tokens.stream().map(ComposerService::decodeComposer).collect(Collectors.toSet());
    }
}
